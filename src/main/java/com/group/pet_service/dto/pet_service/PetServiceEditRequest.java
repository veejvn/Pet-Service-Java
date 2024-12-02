package com.group.pet_service.dto.pet_service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceEditRequest {
    String name;
    Integer price;
    String description;
    String image;
    MultipartFile imageFile;
}
