package com.group.pet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.group.pet_service.dto.serviceDto.ServiceDTO;
import com.group.pet_service.model.Service;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceDTO toDTO(Service service);
    Service toEntity(ServiceDTO dto);
    void updateEntityFromDTO(ServiceDTO dto, @MappingTarget Service service);
}