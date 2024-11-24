package com.group.pet_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.group.pet_service.dto.request.ServiceCreationRequest;
import com.group.pet_service.dto.request.ServiceEditRequest;
import com.group.pet_service.dto.response.ServiceResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.ServiceImage;
import com.group.pet_service.repository.ServiceImageRepository;
import com.group.pet_service.repository.PetServiceRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetServiceService {
    Cloudinary cloudinary;
    PetServiceRepository petServiceRepository;
    ServiceMapper serviceMapper;
    ServiceImageRepository serviceImageRepository;

    public PetService createService(ServiceCreationRequest request) {
        var serviceChecker = petServiceRepository.findByName(request.getName());
        if (serviceChecker.isPresent())
            throw new AppException(ErrorCode.SERVICE_EXISTED);

        PetService petService = serviceMapper.toService(request);

        petService.setCreateAt(new Timestamp(System.currentTimeMillis()));

        return petServiceRepository.save(petService);
    }

    @Transactional
    public void addImagesToService(String serviceId, MultipartFile[] images) throws IOException {
        // Find the PetService by ID or throw an exception if not found
        PetService petService = petServiceRepository.findById(serviceId)
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

    @Transactional
    public PetService updateService(ServiceEditRequest request) {
        PetService existingService = petServiceRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        // Check for unique name if changed
        if (!existingService.getName().equals(request.getName())) {
            var nameChecker = petServiceRepository.findByName(request.getName());
            if (nameChecker.isPresent()) {
                throw new AppException(ErrorCode.SERVICE_EXISTED);
            }
            existingService.setName(request.getName());
        }

        existingService.setDescription(request.getDescription());
        existingService.setPrice(request.getPrice());
        existingService.setDisabled(request.isDisabled());

        return petServiceRepository.save(existingService);
    }

    public PetService getServiceById(String id) {
        return petServiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
    }

    public List<ServiceResponse> getAll() {
        return petServiceRepository.findAll().stream().map(serviceMapper::toServiceResponse).toList();
    }

    public void deleteService(String id) {
        PetService service = petServiceRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
        petServiceRepository.delete(service);
    }
}
