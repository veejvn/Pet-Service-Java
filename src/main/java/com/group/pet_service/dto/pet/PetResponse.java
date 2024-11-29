package com.group.pet_service.dto.pet;

import com.group.pet_service.dto.species.SpeciesResponse;
import com.group.pet_service.model.PetImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {
    String id;
    String name;
    Float weight;
    Integer height;
    String description;
    SpeciesResponse species;
    String image;
}
