package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.dto.response.ServiceResponse;
import com.group.pet_service.model.PetService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    PetService toService(ServiceCreationRequest request);
    ServiceResponse toServiceResponse(PetService service);
}
