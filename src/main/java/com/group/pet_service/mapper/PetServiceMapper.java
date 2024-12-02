package com.group.pet_service.mapper;

import com.group.pet_service.dto.pet_service.PetServiceEditRequest;
import com.group.pet_service.dto.pet.PetServiceRequest;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.model.PetService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetServiceMapper {
    PetServiceResponse toDTO(PetService petService);

    @Mapping(target = "image", ignore = true)
    PetService toEntity(PetServiceRequest request);

    @Mapping(target = "image", ignore = true)
    void updateEntityFromDTO(@MappingTarget PetService petService, PetServiceEditRequest request);

    PetServiceResponse toPetServiceResponse(PetService petService);

    default Page<PetServiceResponse> toPetServiceResponsePage(Page<PetService> petServicePage) {
        List<PetServiceResponse> petServiceResponses = petServicePage.getContent().stream()
                .map(this::toPetServiceResponse).toList();
        return new PageImpl<>(petServiceResponses, petServicePage.getPageable(), petServicePage.getTotalPages());
    }

    ;

    PetServiceEditRequest toPetServiceEditRequest(PetService petService);
}