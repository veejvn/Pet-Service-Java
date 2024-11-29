package com.group.pet_service.dto.pet_service;

import com.group.pet_service.model.ServiceImage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceResponse {
    String id;
    String name;
    Double price;
    String description;
    String image;
}
