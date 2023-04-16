package com.isep.acme.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isep.acme.domain.model.Product;
import com.isep.acme.domain.model.Review;
import com.isep.acme.domain.model.enumarate.ApprovalStatus;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByProduct(Product product);

    List<Review> findByApprovalStatus(ApprovalStatus approvalStatus);

    List<Review> findByProductAndApprovalStatus(Product product, ApprovalStatus approvalStatus);

    List<Review> findByUser(String user);
}
