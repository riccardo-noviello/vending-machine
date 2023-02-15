package com.mvp.backend.repository;

import com.mvp.backend.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByIdThenReturnProduct() {
        // Given
        Product product = Product.builder().productName("Chips").amountAvailable(0).cost(5).sellerId(1L).build();
        productRepository.save(product);

        // When
        Optional<Product> found = productRepository.findById(product.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("Chips", found.get().getProductName());
        assertEquals(0, found.get().getAmountAvailable());
        assertEquals(5, found.get().getCost());
        assertEquals(1L, found.get().getSellerId());
    }
}