package com.group.pet_service.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Long id;

    @NotNull(message = "Service ID is required")
    private String serviceId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}