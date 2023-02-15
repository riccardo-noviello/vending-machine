package com.mvp.backend.repository;

import com.mvp.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
    List<Product> findBySellerId(Long sellerId);
}
