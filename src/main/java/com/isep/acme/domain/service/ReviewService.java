package com.isep.acme.domain.service;

import java.util.List;

import com.isep.acme.domain.model.*;
import com.isep.acme.dto.ReviewDTO;
import com.isep.acme.dto.request.CreateReviewDTO;

public interface ReviewService {

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    Double getWeightedAverage(Product product);

    Boolean DeleteReview(Long reviewId);

    List<ReviewDTO> findPendingReview();

    ReviewDTO moderateReview(Long reviewID, String approved);

    List<ReviewDTO> findReviewsByUser(Long userID);
}
