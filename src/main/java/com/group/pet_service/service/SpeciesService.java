package com.group.pet_service.service;

import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.dto.request.SpeciesEditRequest;
import com.group.pet_service.dto.request.SpeciesRequest;
import com.group.pet_service.dto.response.SpeciesResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.exception.ErrorCode;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.Species;
import com.group.pet_service.repository.ServiceRepository;
import com.group.pet_service.repository.SpeciesRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpeciesService {
    SpeciesRepository speciesRepository;

    public void createSpecies(SpeciesRequest request) {
        var speciesChecker = speciesRepository.findByName(request.getName());
        if (speciesChecker.isPresent())
            throw new AppException(ErrorCode.SPECIES_EXISTED);

        Species newSpecies = Species.builder()
                .description(request.getDescription())
                .name(request.getName())
                .build();

        speciesRepository.save(newSpecies);
    }

    @Transactional
    public Species updateSpecies(SpeciesEditRequest request) {
        Species existingSpecies = speciesRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SPECIES_NOT_EXISTED));

        // Check for unique name if changed
        if (!existingSpecies.getName().equals(request.getName())) {
            var nameChecker = speciesRepository.findByName(request.getName());
            if (nameChecker.isPresent()) {
                throw new AppException(ErrorCode.SPECIES_EXISTED);
            }
            existingSpecies.setName(request.getName());
        }

        existingSpecies.setDescription(request.getDescription());
        return speciesRepository.save(existingSpecies);
    }
    @Transactional
    public void deleteSpecies(String id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIES_NOT_EXISTED));

        speciesRepository.delete(species);
    }
    public Species getSpeciesById(String id) {
        return speciesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIES_NOT_FOUND));
    }


    public List<SpeciesResponse> getAll(){
        return speciesRepository.findAll().stream().map(species -> {
            return SpeciesResponse.builder()
                    .id(species.getId())
                    .name(species.getName())
                    .description(species.getDescription())
                    .build();
        }).toList();
    }
}
