package com.group.pet_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {
    @NotNull(message = "Email is required")
    @Email(message = "Email invalid")
    String email;
    @NotNull(message = "Password is required")
    @Size(min = 4, message = "Password must be longer than 4 letters")
    String password;
}
