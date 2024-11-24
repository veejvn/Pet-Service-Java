package com.group.pet_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminLoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email invalid")
    String email;

    @NotBlank(message = "Password is required")
    String password;
}
