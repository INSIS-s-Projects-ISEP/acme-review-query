package com.isep.acme.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.dto.ReviewDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@AllArgsConstructor
class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "finds a product through its sku and shows its review by status")
    @GetMapping("/products/{sku}/reviews/{status}")
    public ResponseEntity<List<ReviewDTO>> findById(@PathVariable(value = "sku") String sku, @PathVariable(value = "status") String status) {
        var review = reviewService.getReviewsOfProduct(sku, status);
        return ResponseEntity.ok().body(review);
    }

    @Operation(summary = "gets review by user")
    @GetMapping("/reviews/{userId}")
    public ResponseEntity<List<ReviewDTO>> findReviewByUser(@PathVariable(value = "userId") Long userId) {
        var review = reviewService.findReviewsByUser(userId);
        return ResponseEntity.ok().body(review);
    }

    @Operation(summary = "gets pedding reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<List<ReviewDTO>> getPendingReview(){
        List<ReviewDTO> reviews = reviewService.findPendingReview();
        return ResponseEntity.ok().body(reviews);
    }
    
}
