package com.group.pet_service.dto.pet_service;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceEditRequest {
    String id;
    String name;
    String description;
    Double price;
    Double disabled;
}
