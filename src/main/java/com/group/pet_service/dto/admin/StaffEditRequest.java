package com.group.pet_service.dto.admin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffEditRequest {
    String displayName;
    String phoneNumber;
    String avatar;
    MultipartFile avatarFile;
    String jobPositionId;
}
