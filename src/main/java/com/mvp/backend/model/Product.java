package com.mvp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.mvp.backend.Constants.MULTIPLE_OF_FIVE;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "productName is required")
    private String productName;

    @Pattern(regexp = MULTIPLE_OF_FIVE)
    private int cost;

    @PositiveOrZero
    private int amountAvailable;

    @NotNull(message = "sellerId is required")
    private Long sellerId;
}
