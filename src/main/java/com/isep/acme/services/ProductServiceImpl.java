package com.isep.acme.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.isep.acme.model.Product;
import com.isep.acme.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return repository.findBySku(sku);
    }

    @Override
    public void deleteBySku(String sku) {
        repository.deleteBySku(sku);
    }

}
