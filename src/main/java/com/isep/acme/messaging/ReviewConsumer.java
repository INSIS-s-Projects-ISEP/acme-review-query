package com.isep.acme.messaging;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.service.ReviewService;
import com.isep.acme.dto.mapper.ReviewMapper;
import com.isep.acme.dto.message.ReviewMessage;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ReviewConsumer {
    
    private final String instanceId;
    private final MessageConverter messageConverter;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @RabbitListener(queues = "#{reviewCreatedQueue.name}", ackMode = "MANUAL")
    public void reviewCreated(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
        
        MessageProperties messageProperties = message.getMessageProperties();
        if(messageProperties.getAppId().equals(instanceId)){
            channel.basicAck(tag, false);
            log.info("Received own message and ignored it.");
            return;
        }

        ReviewMessage reviewMessage = (ReviewMessage) messageConverter.fromMessage(message);
        log.info("Review received: " + reviewMessage.getIdReview());

        Review review = reviewMapper.toEntity(reviewMessage);
        reviewService.save(review);
        log.info("Review created: " + reviewMessage.getIdReview());

        channel.basicAck(tag, false);
    }
    
}
