package com.group.pet_service.dto.pet;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetUpdateRequest {
    String name;

    Long weight;

    String age;

    String description;

    String image;

    String species_id;
}
