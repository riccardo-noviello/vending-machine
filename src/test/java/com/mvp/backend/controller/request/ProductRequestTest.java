package com.mvp.backend.controller.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductRequestTest {

    @Test
    public void validateProductIsValid() {
        ProductRequest validProductRequest1 = new ProductRequest("Chips", "0", 0);
        assertEquals(0, getViolations(validProductRequest1).size());

        ProductRequest validProductRequest2 = new ProductRequest("Chips", "5", 0);
        assertEquals(0, getViolations(validProductRequest2).size());

        ProductRequest validProductRequest3 = new ProductRequest("Chips", "10", 0);
        assertEquals(0, getViolations(validProductRequest3).size());
    }

    @Test
    public void validateProductIsInvalid() {
        ProductRequest validProductRequest1 = new ProductRequest("Chips", "-1", 0);
        assertEquals(1, getViolations(validProductRequest1).size());

        ProductRequest validProductRequest2 = new ProductRequest("Chips", "a", 0);
        assertEquals(1, getViolations(validProductRequest2).size());

        ProductRequest validProductRequest3 = new ProductRequest("Chips", "0", -5);
        assertEquals(1, getViolations(validProductRequest3).size());
    }

    private static Set<ConstraintViolation<ProductRequest>> getViolations(ProductRequest productRequest) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        return violations;
    }
}