package com.group.pet_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.group.pet_service.dto.request.PetRequest;
import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.exception.ErrorCode;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.*;
import com.group.pet_service.repository.PetRepository;
import com.group.pet_service.repository.ServiceRepository;
import com.group.pet_service.repository.SpeciesRepository;
import com.group.pet_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetService {
    Cloudinary cloudinary;
    PetRepository petRepository;
    UserRepository userRepository;
    SpeciesRepository speciesRepository;
    private final PetImageRepository petImageRepository;

    public void createPet(PetRequest request){
        User ownerChecker = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        Species speciesChecker = speciesRepository.findById(request.getSpeciesId())
                .orElseThrow(() ->  new AppException(ErrorCode.SPECIES_NOT_EXISTED));

        Pet newPet = Pet.builder()
                .description(request.getDescription())
                .height(request.getHeight())
                .name(request.getName())
                .weight(request.getWeight())
                .species(speciesChecker)
                .user(ownerChecker)
                .build();

        petRepository.save(newPet);
    }

    @Transactional
    public void addImagesToPet(String petId, MultipartFile[] images) throws IOException {
        // Find the PetService by ID or throw an exception if not found
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new AppException(ErrorCode.PET_NOT_FOUND));
//        testCloudinary();
        Set<PetImage> petImages = new HashSet<>();

        for (MultipartFile image : images) {
            try {
                // Upload image to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader()
                        .upload(image.getBytes(), ObjectUtils.asMap("folder", "pet_service_images/"));

                // Extract the secure URL of the uploaded image
                String url = (String) uploadResult.get("secure_url");

                // Create a ServiceImage entity and associate it with the PetService
                PetImage petImage = PetImage.builder()
                        .url(url)
                        .pet(pet)
                        .build();

                petImages.add(petImage);
            } catch (Exception e) {
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        petImageRepository.saveAll(petImages);
    }
}
