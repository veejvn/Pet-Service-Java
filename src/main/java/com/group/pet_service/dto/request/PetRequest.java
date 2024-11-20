package com.group.pet_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetRequest {
    String name;
    String description;
    String userId;
    String speciesId;
    int height;
    double weight;
}
