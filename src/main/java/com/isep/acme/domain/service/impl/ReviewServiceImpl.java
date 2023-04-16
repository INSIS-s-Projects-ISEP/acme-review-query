package com.isep.acme.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.isep.acme.domain.model.Product;
import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.model.enumarate.ApprovalStatus;
import com.isep.acme.domain.repository.ProductRepository;
import com.isep.acme.domain.repository.ReviewRepository;
import com.isep.acme.domain.service.RestService;
import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.exception.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestService restService;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsOfProduct(String sku, ApprovalStatus approvalStatus) {
        Product product = productRepository.findBySku(sku).orElseThrow();
        return reviewRepository.findByProductAndApprovalStatus(product, approvalStatus);
    }

    @Override
    public Double getWeightedAverage(Product product){

        List<Review> reviews = reviewRepository.findByProduct(product);
        if(reviews.isEmpty()){
            return 0.0;
        }

        double sum = 0;
        for(Review review: reviews){
            sum += review.getRate();
        }

        return sum/reviews.size();
    }

    @Override
    public List<Review> findPendingReview(){
        return reviewRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    @Override
    public List<Review> findReviewsByUser(String user) {
        return reviewRepository.findByUser(user);
    }

    @Override
    public Review createReviewForProduct(Review review, Product product){
        String funfact = restService.getFunFact(review.getPublishingDate());
        
        review.setProduct(product);
        review.setFunFact(funfact);
        return save(review);
    }

    @Override
    public Review save(Review review){
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId){
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public Review moderateReview(Long reviewID, ApprovalStatus approvalStatus){

        Review review = reviewRepository.findById(reviewID).orElseThrow(() -> {
            throw new ResourceNotFoundException("Review not found");
        });

        review.setApprovalStatus(approvalStatus);
        return reviewRepository.save(review);
    }

}