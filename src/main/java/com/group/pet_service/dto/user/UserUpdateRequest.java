package com.group.pet_service.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String displayName;
    boolean gender;
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dob;
    String phoneNumber;
    String avatar;
}
