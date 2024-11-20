package com.group.pet_service.service;

import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.dto.request.SpeciesRequest;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.exception.ErrorCode;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.Species;
import com.group.pet_service.repository.ServiceRepository;
import com.group.pet_service.repository.SpeciesRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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

}
