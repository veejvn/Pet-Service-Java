package com.group.pet_service.mapper;

import com.group.pet_service.dto.pet.PetRequest;
import com.group.pet_service.dto.pet.PetResponse;
import com.group.pet_service.dto.pet.PetUpdateRequest;
import com.group.pet_service.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {
    Pet toPet(PetRequest request);

    PetResponse toPetResponse(Pet pet);

    List<PetResponse> toListPetResponse(List<Pet> pets);

    void toPetUpdateResponse(@MappingTarget Pet pet, PetUpdateRequest request);
}
