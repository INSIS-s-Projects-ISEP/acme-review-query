package com.isep.acme.domain.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.isep.acme.api.controllers.ResourceNotFoundException;
import com.isep.acme.domain.model.Product;
import com.isep.acme.domain.model.Rating;
import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.model.User;
import com.isep.acme.domain.repository.ProductRepository;
import com.isep.acme.domain.repository.ReviewRepository;
import com.isep.acme.domain.repository.UserRepository;
import com.isep.acme.domain.service.RatingService;
import com.isep.acme.domain.service.RestService;
import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.domain.service.UserService;
import com.isep.acme.dto.ReviewDTO;
import com.isep.acme.dto.mapper.ReviewMapper;
import com.isep.acme.dto.request.CreateReviewDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RatingService ratingService;
    private final UserService userService;
    private final RestService restService;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public Iterable<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public ReviewDTO create(CreateReviewDTO createReviewDTO, String sku) {

        Optional<Product> product = productRepository.findBySku(sku);
        if(product.isEmpty()){
            return null;
        }

        var user = userService.getUserId(createReviewDTO.getUserID());
        if(user.isEmpty()){
            return null;
        }

        Rating rating = null;
        Optional<Rating> r = ratingService.findByRate(createReviewDTO.getRating());
        if(r.isPresent()){
            rating = r.get();
        }

        LocalDate date = LocalDate.now();
        String funfact = restService.getFunFact(date);
        if(funfact == null){
            return null;
        }

        Review review = new Review(createReviewDTO.getReviewText(), date, product.get(), funfact, rating, user.get());
        review = reviewRepository.save(review);
        if(review == null){
            return null;
        }

        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<Product> product = productRepository.findBySku(sku);
        if(product.isEmpty()){
            return null;
        }

        Optional<List<Review>> optReview = reviewRepository.findByProductIdStatus(product.get(), status);
        if(optReview.isEmpty()){
            return null;
        }

        return ReviewMapper.toDtoList(optReview.get());
    }

    @Override
    public Double getWeightedAverage(Product product){

        Optional<List<Review>> r = reviewRepository.findByProductId(product);
        if(r.isEmpty()){
            return 0.0;
        }

        double sum = 0;
        for(Review rev: r.get()){
            Rating rate = rev.getRating();
            if (rate != null){
                sum += rate.getRate();
            }
        }
        return sum/r.get().size();
    }

    @Override
    public Boolean DeleteReview(Long reviewId)  {

        Optional<Review> rev = reviewRepository.findById(reviewId);
        if (rev.isEmpty()){
            return null;
        }

        Review r = rev.get();
        reviewRepository.delete(r);
        return true;
    }

    @Override
    public List<ReviewDTO> findPendingReview(){

        Optional<List<Review>> r = reviewRepository.findPendingReviews();
        if(r.isEmpty()){
            return null;
        }

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<Review> r = reviewRepository.findById(reviewID);
        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);
        if(!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Review review = reviewRepository.save(r.get());
        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        Optional<User> user = userRepository.findById(userID);
        if(user.isEmpty()){
            return null;
        }

        Optional<List<Review>> r = reviewRepository.findByUserId(user.get());
        if(r.isEmpty()){
            return null;
        }

        return ReviewMapper.toDtoList(r.get());
    }
}