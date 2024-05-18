package com.eisen.foodapp.module.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotNull
        String name,
        @NotNull
        @Min(5)
        String login,
        @NotNull
        @Min(8)
        String password
) {}
