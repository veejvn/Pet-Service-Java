package com.group.pet_service.dto.response;

import com.group.pet_service.model.ServiceImage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    String id;
    String name;
    double price;
    String description;
    Timestamp createAt;
    Set<ServiceImage> images;
}
