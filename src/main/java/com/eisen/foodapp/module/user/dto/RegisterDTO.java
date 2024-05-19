package com.eisen.foodapp.module.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotNull
        String name,
        @NotNull
        @Size(min = 5, message = "{validation.name.size.too_short}")
        String login,
        @NotNull
        @Size(min = 8, message = "{validation.name.size.too_short}")
        String password
) {}
