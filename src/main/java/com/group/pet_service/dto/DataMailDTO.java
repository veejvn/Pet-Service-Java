package com.group.pet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataMailDTO {
    private String to;
    private String subject;
    private String content;
    private Map<String, Object> props;
}