package com.group.pet_service.dto.species;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpeciesResponse {
    String id;
    String name;
    String description;
}
