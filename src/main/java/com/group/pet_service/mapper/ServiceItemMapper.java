package com.group.pet_service.mapper;

import org.mapstruct.Mapper;
import com.group.pet_service.dto.request.ServiceItemRequest;
import com.group.pet_service.model.ServiceItem;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    ServiceItemRequest toDTO(ServiceItem serviceItem);
    ServiceItem toEntity(ServiceItemRequest serviceItemRequest);
}