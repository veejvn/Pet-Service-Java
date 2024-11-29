package com.group.pet_service.dto.pet;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetEditRequest {
    private String id;
    private String name;
    private String description;
    private int height;
    private Double weight;
    private String speciesId;
    private String userId;
}