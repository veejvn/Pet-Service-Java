package com.group.pet_service.dto.job_position;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPositionEditRequest {
    String name;
}
