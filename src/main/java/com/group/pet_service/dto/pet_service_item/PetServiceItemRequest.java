package com.group.pet_service.dto.pet_service_item;

import java.sql.Timestamp;


import com.group.pet_service.enums.ServiceItemStatus;
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
    private ServiceItemStatus status;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private int quantity;

    private double totalCost;
    private Timestamp start;
    private Timestamp end;

    @NotNull(message = "Service ID cannot be null")
    private String serviceId;

    @NotNull(message = "Receipt ID cannot be null")
    private String receiptId;

    @NotNull(message = "Staff ID cannot be null")
    private String staffId;
}