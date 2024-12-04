package com.group.pet_service.dto.user;

import com.group.pet_service.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String displayName;
    LocalDate dob;
    String phoneNumber;
    String avatar;
    String jobPosition;
    Set<Role> roles;
}
