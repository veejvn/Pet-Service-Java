package com.group.e_commerce.dto.response;

import com.group.e_commerce.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
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
    Set<RoleResponse> roles;
}
