package com.group.pet_service.mapper;

import org.mapstruct.Mapper;
import com.group.pet_service.dto.serviceItemDto.ServiceItemDTO;
import com.group.pet_service.model.ServiceItem;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    ServiceItemDTO toDTO(ServiceItem serviceItem);
    ServiceItem toEntity(ServiceItemDTO serviceItemDTO);
}