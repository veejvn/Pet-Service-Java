package com.group.pet_service.service;

import com.group.pet_service.dto.species.SpeciesRequest;
import com.group.pet_service.dto.species.SpeciesResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.SpeciesMapper;
import com.group.pet_service.model.Species;
import com.group.pet_service.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;

    public SpeciesResponse create(SpeciesRequest request) {
        Species species = speciesMapper.toSpecies(request);
        speciesRepository.save(species);
        return speciesMapper.toSpeciesResponse(species);
    }

    public List<SpeciesResponse> getAll() {
        List<Species> species = speciesRepository.findAll();
        return speciesMapper.toListSpeciesResponse(species);
    }

    public SpeciesResponse get(String id) {
        Species species = speciesRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Species not found", "species-e-01")
        );
        return speciesMapper.toSpeciesResponse(species);
    }

    public void delete(String id) {
        speciesRepository.deleteById(id);
    }
}
