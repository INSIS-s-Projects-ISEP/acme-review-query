package com.isep.acme.domain.service;

import java.util.Optional;

import com.isep.acme.domain.model.Rating;

public interface RatingService {

    Optional<Rating> findByRate(Double rate);
}
