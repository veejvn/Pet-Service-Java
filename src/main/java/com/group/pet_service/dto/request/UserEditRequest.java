package com.group.pet_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEditRequest {
    String id;
    String firstname;
    String lastname;
    boolean gender;
    String email;
    LocalDate dob;
    String password;
    String phoneNum;
}
