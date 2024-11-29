package com.group.pet_service.dto.admin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    String id;
    String displayName;
    String phoneNumber;
    String avatar;
    Timestamp createdAt;
    String jobPosition;
}
