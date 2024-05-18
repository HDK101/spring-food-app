package com.eisen.foodapp.module.user.dto;

import java.util.List;

public record SetRolesDTO(
        List<Long> roleIds
) {
}
