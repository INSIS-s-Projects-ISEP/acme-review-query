package com.isep.acme.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.model.enumarate.ApprovalStatus;
import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.dto.mapper.ReviewMapper;
import com.isep.acme.dto.response.ReviewResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@AllArgsConstructor
class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Operation(summary = "finds a product through its sku and shows its review by status")
    @GetMapping("/products/{sku}/reviews/{approvalStatus}")
    public ResponseEntity<List<ReviewResponse>> findById(@PathVariable String sku, @PathVariable ApprovalStatus approvalStatus) {
        List<Review> reviews = reviewService.getReviewsOfProduct(sku, approvalStatus);
        List<ReviewResponse> reviewResponses = reviewMapper.toResponseList(reviews);
        return ResponseEntity.ok().body(reviewResponses);
    }

    @Operation(summary = "gets review by user")
    @GetMapping("/reviews/{user}")
    public ResponseEntity<List<ReviewResponse>> findReviewByUser(@PathVariable String user) {
        List<Review> reviews = reviewService.findReviewsByUser(user);
        List<ReviewResponse> reviewResponses = reviewMapper.toResponseList(reviews);
        return ResponseEntity.ok().body(reviewResponses);
    }

    @Operation(summary = "gets pedding reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<List<ReviewResponse>> getPendingReview(){
        List<Review> reviews = reviewService.findPendingReview();
        List<ReviewResponse> reviewResponses = reviewMapper.toResponseList(reviews);
        return ResponseEntity.ok().body(reviewResponses);
    }
    
}
