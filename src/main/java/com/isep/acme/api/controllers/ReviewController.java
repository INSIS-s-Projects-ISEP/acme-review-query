package com.isep.acme.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.dto.ReviewDTO;
import com.isep.acme.dto.request.CreateReviewDTO;

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

    @Operation(summary = "creates review")
    @PostMapping("/products/{sku}/reviews")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable(value = "sku") String sku, @RequestBody CreateReviewDTO createReviewDTO) {

        var review = reviewService.create(createReviewDTO, sku);
        if(review == null){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<ReviewDTO>(review, HttpStatus.CREATED);
    }

    @Operation(summary = "deletes review")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable(value = "reviewId") Long reviewId) {

        Boolean reviewDeleted = reviewService.DeleteReview(reviewId);
        if (reviewDeleted == null){
            return ResponseEntity.notFound().build();
        }

        if (reviewDeleted == false) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().body(reviewDeleted);
    }

    @Operation(summary = "gets pedding reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<List<ReviewDTO>> getPendingReview(){
        List<ReviewDTO> reviews = reviewService.findPendingReview();
        return ResponseEntity.ok().body(reviews);
    }

    @Operation(summary = "Accept or reject review")
    @PutMapping("/reviews/acceptreject/{reviewId}")
    public ResponseEntity<ReviewDTO> putAcceptRejectReview(@PathVariable(value = "reviewId") Long reviewId, @RequestBody String approved){

        try {
            ReviewDTO rev = reviewService.moderateReview(reviewId, approved);
            return ResponseEntity.ok().body(rev);
        }
        catch( IllegalArgumentException e ) {
            return ResponseEntity.badRequest().build();
        }
        catch( ResourceNotFoundException e ) {
            return ResponseEntity.notFound().build();
        }
    }
}
