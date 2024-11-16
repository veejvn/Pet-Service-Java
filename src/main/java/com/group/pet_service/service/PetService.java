package com.group.pet_service.service;

import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.response.PetResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.PetMapper;
import com.group.pet_service.model.Pet;
import com.group.pet_service.model.PetImage;
import com.group.pet_service.model.Species;
import com.group.pet_service.repository.PetRepository;
import com.group.pet_service.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final SpeciesRepository speciesRepository;

    public PetResponse create(PetRequest request){

        Species species = speciesRepository.findById(request.getSpecies_id())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Species not found", "species-e-1")
                );
        Pet pet = petMapper.toPet(request);
        pet.setSpecies(species);
        Set<PetImage> petImages = new HashSet<>();
        for (String petImageUrl: request.getImageUrls()){
            PetImage image = PetImage.builder().url(petImageUrl).pet(pet).build();
            petImages.add(image);
        }
        pet.setImages(petImages);
        petRepository.save(pet);

        PetResponse petResponse = petMapper.toPetResponse(pet);

        Set<String> imageUrls = new HashSet<>();
        for(PetImage petImage: pet.getImages()){
            imageUrls.add(petImage.getUrl());
        }
        petResponse.setImageUrl(imageUrls);

        petResponse.setSpeciesStr(pet.getSpecies().getName());

        return petResponse;
    }

    public PetResponse get(String id){
        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-1")
        );

        Set<String> imageUrls = new HashSet<>();
        for(PetImage petImage: pet.getImages()){
            imageUrls.add(petImage.getUrl());
        }

        PetResponse petResponse = petMapper.toPetResponse(pet);
        petResponse.setSpeciesStr(pet.getSpecies().getName());
        petResponse.setImageUrl(imageUrls);

        return petResponse;
    }

    public List<PetResponse> getAll(){
        List<Pet> pets = petRepository.findAll();

        List<PetResponse> petResponses = new ArrayList<>();

        for (Pet pet : pets){
            PetResponse petResponse = get(pet.getId());
            petResponses.add(petResponse);
        }

        return petResponses;
    }

    public PetResponse update(PetRequest request, String id){

        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet not found", "pet-e-1")
        );

        Species species = speciesRepository.findById(request.getSpecies_id())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Species not found", "species-e-1")
                );
        petMapper.toPetUpdateResponse(pet, request);
        pet.setSpecies(species);
        Set<PetImage> petImages = new HashSet<>();
        for (String petImageUrl: request.getImageUrls()){
            PetImage image = PetImage.builder().url(petImageUrl).pet(pet).build();
            petImages.add(image);
        }
        pet.setImages(petImages);
        petRepository.save(pet);

        PetResponse petResponse = petMapper.toPetResponse(pet);

        Set<String> imageUrls = new HashSet<>();
        for(PetImage petImage: pet.getImages()){
            imageUrls.add(petImage.getUrl());
        }
        petResponse.setImageUrl(imageUrls);

        petResponse.setSpeciesStr(pet.getSpecies().getName());

        return petResponse;
    }

    public void delete(String id){
        petRepository.deleteById(id);
    }
}
