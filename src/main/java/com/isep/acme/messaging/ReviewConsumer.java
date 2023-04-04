package com.isep.acme.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.dto.request.CreateReviewDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReviewConsumer {
    
    private final ReviewService reviewService;

    @RabbitListener(queues = {"#{reviewCreatedQueue.name}"})
    public void reviewCreated(Review review){
        CreateReviewDTO createReviewDTO = new CreateReviewDTO(review.getReviewText(), review.getUser().getUserId(), review.getRating().getRate());
        reviewService.create(createReviewDTO , review.getProduct().getSku());
    }
}
