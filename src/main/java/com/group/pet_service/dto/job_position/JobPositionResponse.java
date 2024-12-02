package com.group.pet_service.dto.job_position;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPositionResponse {
    String id;
    String name;
}
