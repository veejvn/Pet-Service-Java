package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.response.PetResponse;
import com.group.pet_service.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {
    @Mapping(target = "images", ignore = true)
    Pet toPet(PetRequest request);
    PetResponse toPetResponse(Pet pet);
    List<PetResponse> toListPetResponse(List<Pet> pets);
    @Mapping(target = "images", ignore = true)
    void toPetUpdateResponse (@MappingTarget Pet pet, PetRequest request);
}
