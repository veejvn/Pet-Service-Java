package com.group.pet_service.mapper;

import com.group.pet_service.dto.response.ServiceResponse;
import com.group.pet_service.model.ServiceImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.group.pet_service.dto.request.ServiceRequest;
import com.group.pet_service.model.Service;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceResponse toDTO(Service service);

    @Mapping(target = "images", ignore = true)
    Service toEntity(ServiceRequest request);

    @Mapping(target = "images", ignore = true)
    void updateEntityFromDTO(ServiceRequest request, @MappingTarget Service service);
}