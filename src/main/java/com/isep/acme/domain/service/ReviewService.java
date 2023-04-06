package com.isep.acme.domain.service;

import java.util.List;

import com.isep.acme.domain.model.Product;
import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.model.enumarate.ApprovalStatus;

public interface ReviewService {

    Iterable<Review> findAll();

    List<Review> getReviewsOfProduct(String sku, String status);

    Double getWeightedAverage(Product product);

    List<Review> findPendingReview();

    List<Review> findReviewsByUser(String user);

    Review createReviewForProduct(Review review, Product product);

    Review save(Review review);

    void deleteReview(Long reviewId);

    Review moderateReview(Long reviewID, ApprovalStatus approvalStatus);
}
