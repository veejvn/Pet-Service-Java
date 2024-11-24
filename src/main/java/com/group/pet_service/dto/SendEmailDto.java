package com.group.pet_service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendEmailDto {
    String to;
    String subject;
    String text;
}
