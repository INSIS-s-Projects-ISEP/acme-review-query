package com.isep.acme.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter jackson2JsonMessageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange productCreatedExchange() {
        return new FanoutExchange("product.product-created");
    }

    @Bean
    public Queue productCreatedQueue(String instanceId) {
        return new Queue("product.product-created.review-query." + instanceId, true, true, true);
    }

    @Bean
    public Binding productCreatedBindingProductCreated(FanoutExchange productCreatedExchange,
            Queue productCreatedQueue) {
        return BindingBuilder.bind(productCreatedQueue).to(productCreatedExchange);
    }

    @Bean
    public FanoutExchange productDeletedExchange(){
        return new FanoutExchange("product.product-deleted");
    }

    @Bean
    public Queue productDeletedQueue(String instanceId){
        return new Queue("product.product-deleted.review-query." + instanceId, true, true, true);
    }

    @Bean
    public Binding bindingProductDeletedtoProductDeleted(FanoutExchange productDeletedExchange, Queue productDeletedQueue){
        return BindingBuilder.bind(productDeletedQueue).to(productDeletedExchange);
    }

    @Bean
    public FanoutExchange reviewCreatedExchange() {
        return new FanoutExchange("review.review-created");
    }
    
    @Bean
    public Queue reviewCreatedQueue(String instanceId) {
        return new Queue("review.review-created.review-query." + instanceId, true, true, true);
    }

    @Bean
    public Binding reviewCreatedBindingReviewCreated(FanoutExchange reviewCreatedExchange,
            Queue reviewCreatedQueue) {
        return BindingBuilder.bind(reviewCreatedQueue).to(reviewCreatedExchange);
    }

    @Bean
    public FanoutExchange reviewUpdatedExchange() {
        return new FanoutExchange("review.review-updated");
    }
    
    @Bean
    public Queue reviewUpdatedQueue(String intanceId) {
        return new Queue("review.review-updated.review-command." + intanceId, true, true, true);
    }

    @Bean
    public Binding bindingReviewUpdatedtoReviewUpdated(FanoutExchange reviewUpdatedExchange,
            Queue reviewUpdatedQueue) {
        return BindingBuilder.bind(reviewUpdatedQueue).to(reviewUpdatedExchange);
    }

    @Bean
    public FanoutExchange reviewDeletedExchange() {
        return new FanoutExchange("review.review-deleted");
    }
    
    @Bean
    public Queue reviewDeletedQueue(String intanceId) {
        return new Queue("review.review-deleted.review-command." + intanceId, true, true, true);
    }

    @Bean
    public Binding bindingReviewDeletedtoReviewDeleted(FanoutExchange reviewDeletedExchange,
            Queue reviewDeletedQueue) {
        return BindingBuilder.bind(reviewDeletedQueue).to(reviewDeletedExchange);
    }

    // Bootstrapper
    // Product
    @Bean
    public FanoutExchange rpcProductExchange(){
        return new FanoutExchange("rpc.product.review-query-bootstrapper");
    }
    
    // Review
    @Bean
    public FanoutExchange rpcReviewExchange(){
        return new FanoutExchange("rpc.review.review-query-bootstrapper");
    }

}