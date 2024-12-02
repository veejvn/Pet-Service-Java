package com.group.pet_service.dto.staff;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreationRequest {
    String email;
    String password;
    String displayName;
    String phoneNumber;
    MultipartFile avatar;
    String jobPositionId;
}
