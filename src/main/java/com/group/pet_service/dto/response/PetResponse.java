package com.group.pet_service.dto.response;

import com.group.pet_service.model.PetImage;
import com.group.pet_service.model.Species;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {
    String id;
    String name;
    Long weight;
    Integer height;
    String description;
    SpeciesResponse species;
    Set<PetImage> images;
}
