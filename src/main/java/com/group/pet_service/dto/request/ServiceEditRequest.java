package com.group.pet_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceEditRequest {
    String id;
    String name;
    String description;
    Double price;
    boolean disabled;
}
