package com.group.pet_service.dto.species;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpeciesRequest {

    @NotBlank(message = "Species name is required")
    String name;

    String description;
}
