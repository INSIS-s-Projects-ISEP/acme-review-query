package com.isep.acme.services;

import java.util.Optional;

import com.isep.acme.model.Product;

public interface ProductService {

    Product create(Product product);

    Optional<Product> findBySku(String sku);

    void deleteBySku(String sku);
}
