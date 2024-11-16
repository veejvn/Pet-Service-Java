package com.group.pet_service.dto.request;

import com.group.pet_service.model.ServiceItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class ReceiptCreateRequest {

    @NotBlank(message = "Pet is required")
    String petId;

    @NotEmpty(message = "At least one service item is required")
    @Size(min = 1, message = "Min 1")
    Set<@Valid ServiceItemDTO> items;

    @Data
    public static class ServiceItemDTO{
        @NotBlank(message = "Staff is required")
        String staffId;

        @NotBlank(message = "Service is required")
        String serviceId;

        @NotNull(message = "Quantity is required")
        int quantity;

        @NotNull(message = "Start time is required")
        Timestamp start;

        @NotNull(message = "Start end is required")
        Timestamp end;
    }
}
