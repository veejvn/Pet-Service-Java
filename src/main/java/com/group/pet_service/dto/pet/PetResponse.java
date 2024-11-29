package com.group.pet_service.dto.pet;

import com.group.pet_service.dto.species.SpeciesResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {
    String id;
    String name;
    Double weight;
    String age;
    String description;
    SpeciesResponse species;
    String image;
}
