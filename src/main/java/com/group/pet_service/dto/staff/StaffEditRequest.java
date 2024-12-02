package com.group.pet_service.dto.staff;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

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
