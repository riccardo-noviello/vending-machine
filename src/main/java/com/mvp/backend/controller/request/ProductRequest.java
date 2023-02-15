package com.mvp.backend.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import static com.mvp.backend.Constants.MULTIPLE_OF_FIVE;


public record ProductRequest(
        @NotEmpty String productName, @Pattern(regexp = MULTIPLE_OF_FIVE) String cost, @PositiveOrZero int amountAvailable) {
}
