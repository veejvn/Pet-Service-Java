package com.group.pet_service.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyForgotPasswordRequest {
    @NotBlank(message = "New password is required")
    private String newPassword;
    @NotBlank(message = "Code cannot is required")
    private String code;
}
