package com.group.pet_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.exception.ErrorCode;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.Pet;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.ServiceImage;
import com.group.pet_service.repository.ServiceImageRepository;
import com.group.pet_service.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetServiceService {
    Cloudinary cloudinary;
    ServiceRepository serviceRepository;
    ServiceMapper serviceMapper;
    ServiceImageRepository serviceImageRepository;

    public PetService createService(ServiceCreationRequest request){
        var serviceChecker = serviceRepository.findByName(request.getName());
        if (serviceChecker.isPresent())
            throw new AppException(ErrorCode.SERVICE_EXISTED);

        PetService petService = serviceMapper.toService(request);

        petService.setCreateAt(new Timestamp(System.currentTimeMillis()));

        return serviceRepository.save(petService);
    }
    @Transactional
    public void addImagesToService(String serviceId, MultipartFile[] images) throws IOException {
        // Find the PetService by ID or throw an exception if not found
        PetService petService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
//        testCloudinary();
        Set<ServiceImage> serviceImages = new HashSet<>();

        for (MultipartFile image : images) {
            try {
                // Upload image to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader()
                        .upload(image.getBytes(), ObjectUtils.asMap("folder", "pet_service_images/"));

                // Extract the secure URL of the uploaded image
                String url = (String) uploadResult.get("secure_url");

                // Create a ServiceImage entity and associate it with the PetService
                ServiceImage serviceImage = ServiceImage.builder()
                        .url(url)
                        .petService(petService)
                        .build();

                serviceImages.add(serviceImage);
            } catch (Exception e) {
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        serviceImageRepository.saveAll(serviceImages);
    }
}
