package com.group.pet_service.dto.serviceDto;

import java.sql.Timestamp;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private String id;

    @NotBlank(message = "Service name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private boolean disabled;
    private String description;
    private Timestamp createAt;
    private Set<ServiceImageDTO> images;
}
