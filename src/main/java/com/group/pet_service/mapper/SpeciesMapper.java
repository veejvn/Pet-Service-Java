package com.group.pet_service.mapper;

import com.group.pet_service.dto.species.SpeciesEditRequest;
import com.group.pet_service.dto.species.SpeciesRequest;
import com.group.pet_service.dto.species.SpeciesResponse;
import com.group.pet_service.model.Species;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {
    Species toSpecies(SpeciesRequest request);

    SpeciesResponse toSpeciesResponse(Species species);

    List<SpeciesResponse> toListSpeciesResponse(List<Species> species);

    SpeciesEditRequest toSpeciesEditRequest(Species species);

    void updateSpecies(@MappingTarget Species species, SpeciesEditRequest request);
}
