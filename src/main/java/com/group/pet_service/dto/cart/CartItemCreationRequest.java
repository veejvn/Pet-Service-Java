package com.group.pet_service.dto.cart;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemCreationRequest {
    @NotBlank(message = "Pet service is required")
    String petServiceId;
}
