package com.mvp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "productName is required")
    private String productName;

    @NotNull(message = "cost is required")
    private int cost;

    @NotNull(message = "amountAvailable is required")
    private int amountAvailable;

    @NotNull(message = "sellerId is required")
    private Long sellerId;
}
