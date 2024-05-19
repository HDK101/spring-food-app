package com.eisen.foodapp.module.order.dto;

import java.util.List;

public record CreateOrderDTO(
        List<Long> foodIds
) {
}
