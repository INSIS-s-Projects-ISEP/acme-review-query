package com.isep.acme.domain.service;

import java.util.Optional;

import com.isep.acme.domain.model.Product;

public interface ProductService {

    Product create(Product product);

    Optional<Product> findBySku(String sku);

    void deleteBySku(String sku);
}
