package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.SpeciesRequest;
import com.group.pet_service.dto.response.SpeciesResponse;
import com.group.pet_service.model.Species;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {
    Species toSpecies(SpeciesRequest request);
    SpeciesResponse toSpeciesResponse(Species species);
    List<SpeciesResponse> toListSpeciesResponse(List<Species> species);
}
