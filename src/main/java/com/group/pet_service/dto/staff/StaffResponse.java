package com.group.pet_service.dto.staff;

import com.group.pet_service.dto.admin.JobPositionResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    String displayName;
    String avatar;
    String phoneNumber;
    JobPositionResponse jobPosition;
}
