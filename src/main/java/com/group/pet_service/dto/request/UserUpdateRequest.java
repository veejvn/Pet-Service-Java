package com.group.pet_service.dto.request;

import com.group.pet_service.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String firstname;
    String lastname;
    boolean gender;
    Date dob;
    String password;
    String phoneNum;
    String email;

    Set<Role> roles;
}
