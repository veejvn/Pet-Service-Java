package com.group.pet_service.dto.staff;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    String id;
    String displayName;
    String phoneNumber;
    String avatar;
    String jobPosition;
}
