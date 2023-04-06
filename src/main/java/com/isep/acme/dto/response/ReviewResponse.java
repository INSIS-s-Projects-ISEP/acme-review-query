package com.isep.acme.dto.response;

import java.time.LocalDate;

import com.isep.acme.domain.model.enumarate.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ReviewResponse {

    private Long idReview;
    private String reviewText;
    private LocalDate publishingDate;
    private ApprovalStatus approvalStatus;
    private String funFact;
    private Double rate;

}
