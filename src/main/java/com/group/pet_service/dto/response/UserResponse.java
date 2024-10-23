package com.group.pet_service.dto.response;

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
    String firstname;
    String lastname;
    boolean gender;
    LocalDate dob;
    String username;
    String phoneNum;
    String email;
    Timestamp createdAt;
    Set<Role> roles;
}
