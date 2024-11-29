package com.group.pet_service.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    String name;

    @NotNull(message = "Pet weight is required")
    Long weight;

    @NotNull(message = "Pet age is required")
    String age;

    String description;

    @NotNull(message = "Pet image is required")
    String image;

    @NotBlank(message = "Pet species is required")
    String species_id;

}
