package com.group.pet_service.dto.pet_service_item;

import com.group.pet_service.enums.PetServiceItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStatusPetServiceItemRequest {
    @NotNull(message = "Pet service status is required")
    PetServiceItemStatus status;
}
