package com.group.pet_service.dto.job_position;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPositionRequest {
    @NotBlank(message = "Job position name is required")
    String name;
}
