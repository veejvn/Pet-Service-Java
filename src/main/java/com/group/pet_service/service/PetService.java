package com.group.pet_service.service;

import com.group.pet_service.dto.pet.PetRequest;
import com.group.pet_service.dto.pet.PetResponse;
import com.group.pet_service.dto.pet.PetUpdateRequest;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.PetMapper;
import com.group.pet_service.model.Pet;
import com.group.pet_service.model.Species;
import com.group.pet_service.repository.PetRepository;
import com.group.pet_service.repository.SpeciesRepository;
import com.group.pet_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final SpeciesRepository speciesRepository;
    private final UploadService uploadService;
    private final UserUtil userUtil;

    public PetResponse create(PetRequest request) {

        Species species = speciesRepository.findById(request.getSpecies_id())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Species not found", "species-e-1")
                );
        Pet pet = petMapper.toPet(request);
        pet.setSpecies(species);
        petRepository.save(pet);

        return petMapper.toPetResponse(pet);
    }

    public PetResponse get(String id) {
        String userId = userUtil.getUserId();

        if (isPetCreator(id, userId)) {
            throw new AppException(HttpStatus.FORBIDDEN, "You art not the creator of this pet", "pet-s-02");
        }

        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-1")
        );

        return petMapper.toPetResponse(pet);
    }

    public List<PetResponse> getAll() {
        List<Pet> pets = petRepository.findAll();

        return petMapper.toListPetResponse(pets);
    }

    public PetResponse update(PetUpdateRequest request, String id) {

        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-1")
        );

        Species species = speciesRepository.findById(request.getSpecies_id())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Species not found", "species-e-1")
                );
        petMapper.toPetUpdateResponse(pet, request);
        pet.setSpecies(species);
        petRepository.save(pet);

        return petMapper.toPetResponse(pet);
    }

    public void delete(String id) {
        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-01")
        );
        try {
            uploadService.deleteFile(pet.getImage());
        } catch (IOException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Error while deleting photo", "pet-e-02");
        }
        petRepository.deleteById(id);
    }

    public boolean isPetCreator(String petId, String userId) {
        return petRepository.findByIdAndUserId(petId, userId).isEmpty();
    }
}
