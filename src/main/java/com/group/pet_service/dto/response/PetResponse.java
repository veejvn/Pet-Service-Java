package com.group.pet_service.dto.response;

import com.group.pet_service.model.PetImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {
    String id;
    String name;
    Double weight;
    Integer height;
    String description;
    SpeciesResponse species;
    Set<PetImage> images;
}
