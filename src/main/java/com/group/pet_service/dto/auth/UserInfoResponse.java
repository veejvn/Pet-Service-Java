package com.group.pet_service.dto.auth;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserInfoResponse {
    private String id;
    private String displayName;
    private String email;
    private String avatar;
    private Set<String> roles;
}
