package com.group.pet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.group.pet_service.dto.request.ServiceRequest;
import com.group.pet_service.model.Service;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceRequest toDTO(Service service);
    Service toEntity(ServiceRequest request);
    void updateEntityFromDTO(ServiceRequest request, @MappingTarget Service service);
}