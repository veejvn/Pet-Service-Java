package com.group.pet_service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpgradeToStaffRequest {
    @NotBlank(message = "User id is required")
    private String id;
}
