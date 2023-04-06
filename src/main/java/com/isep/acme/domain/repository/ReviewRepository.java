package com.isep.acme.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.acme.domain.model.Product;
import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.model.enumarate.ApprovalStatus;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByApprovalStatus(ApprovalStatus approvalStatus);

    List<Review> findByProductAndApprovalStatus(Product product, String status);

    List<Review> findByUser(String user);
}
