package com.group.pet_service.mapper;

import com.group.pet_service.dto.response.ServiceResponse;
import com.group.pet_service.model.PetService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.group.pet_service.dto.request.ServiceRequest;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceResponse toDTO(PetService petService);

    @Mapping(target = "images", ignore = true)
    PetService toEntity(ServiceRequest request);

    @Mapping(target = "images", ignore = true)
    void updateEntityFromDTO(ServiceRequest request, @MappingTarget PetService petService);
}