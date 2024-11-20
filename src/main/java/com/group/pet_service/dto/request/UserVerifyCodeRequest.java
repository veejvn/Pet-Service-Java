package com.group.pet_service.dto.request;

public record UserVerifyCodeRequest(
        String code,
        String userId
        )
{ }
