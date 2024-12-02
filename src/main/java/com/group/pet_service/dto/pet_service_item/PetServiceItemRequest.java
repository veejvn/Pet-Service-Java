package com.group.pet_service.dto.pet_service_item;

import java.time.LocalDateTime;


import com.group.pet_service.enums.PetServiceItemStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetServiceItemRequest {
    private String id;

    @NotNull(message = "Status cannot be null")
    private PetServiceItemStatus status;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private int quantity;

    private double totalCost;
    private LocalDateTime start;
    private LocalDateTime end;

    @NotNull(message = "Service ID cannot be null")
    private String serviceId;

    @NotNull(message = "Receipt ID cannot be null")
    private String receiptId;

    @NotNull(message = "Staff ID cannot be null")
    private String staffId;
}