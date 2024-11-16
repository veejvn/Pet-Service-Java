package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.response.PetResponse;
import com.group.pet_service.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {
    Pet toPet(PetRequest request);
    PetResponse toPetResponse(Pet pet);
    List<PetResponse> toListPetResponse(List<Pet> pets);
    void toPetUpdateResponse (@MappingTarget Pet pet, PetRequest request);
}
