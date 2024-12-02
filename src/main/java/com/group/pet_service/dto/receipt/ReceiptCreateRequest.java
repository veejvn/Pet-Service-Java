package com.group.pet_service.dto.receipt;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ReceiptCreateRequest {

    @NotBlank(message = "Pet is required")
    String petId;

    @NotEmpty(message = "At least one service item is required")
    @Size(min = 1, message = "Min 1")
    Set<@Valid PetServiceItemDTO> items;

    @Data
    public static class PetServiceItemDTO {
        @NotBlank(message = "Staff is required")
        String staffId;

        @NotBlank(message = "Service is required")
        String serviceId;

        @NotNull(message = "Start time is required")
        LocalDateTime start;

        @NotNull(message = "Start end is required")
        LocalDateTime end;
    }
}
