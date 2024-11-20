package com.group.pet_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group.pet_service.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
