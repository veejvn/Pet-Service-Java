package com.group.pet_service.dto.pet_service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceResponse {
    String id;
    String name;
    Integer price;
    String description;
    String image;
}
