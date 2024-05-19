package com.eisen.foodapp.module.food.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public record CreateFoodDTO(
        @Valid
        String name,
        @Min(1)
        Long priceInCents
) {
}
