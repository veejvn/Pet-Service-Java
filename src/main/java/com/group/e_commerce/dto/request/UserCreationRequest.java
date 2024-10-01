package com.group.e_commerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String firstname;
    String lastname;
    boolean gender;
    LocalDate dob;
    String username;
    String password;
    String phoneNum;
    String email;
    Timestamp createdAt;
}
