package com.group.pet_service.dto.pet_service;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceCreationRequest {
    String name;
    String description;
    Double price;
    Double disabled;
    MultipartFile imageFile;
}
