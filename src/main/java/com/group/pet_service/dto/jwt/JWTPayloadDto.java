package com.group.pet_service.dto.jwt;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTPayloadDto {
    String id;
    String email;
    String scope;
}
