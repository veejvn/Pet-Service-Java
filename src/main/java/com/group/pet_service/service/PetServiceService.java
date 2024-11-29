package com.group.pet_service.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.group.pet_service.dto.pet_service.PetServiceCreationRequest;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.PetServiceMapper;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.ServiceImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.group.pet_service.dto.pet.PetServiceRequest;
import com.group.pet_service.repository.PetServiceRepository;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PetServiceService {
    private final PetServiceRepository petServiceRepository;
    private final PetServiceMapper petServiceMapper;
    private final UploadService uploadService;

    public Page<PetServiceResponse> getAllServices(Pageable pageable) {
        return petServiceRepository.findAll(pageable)
                .map(petServiceMapper::toDTO);
    }

    public PetServiceResponse getServiceById(String id) {
        return petServiceRepository.findById(id)
                .map(petServiceMapper::toDTO)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Pet service not found", "pet-service-s-01"));
    }

    @Transactional
    public void createPetService(PetServiceRequest request) throws IOException {
        PetService petService = petServiceMapper.toEntity(request);
        String image = uploadService.uploadFile(request.getImageFile());
        petService.setImage(image);
        petServiceRepository.save(petService);
    }

    @Transactional
    public PetServiceResponse updateService(String id, PetServiceRequest petServiceRequest) {
        // Tìm dịch vụ theo ID
        PetService existingService = petServiceRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Pet service not found", "pet-service-s-01"));

        // Cập nhật thông tin từ ServiceDTO vào thực thể đã tồn tại
        petServiceMapper.updateEntityFromDTO(petServiceRequest, existingService);

        // Lưu và cập nhật dịch vụ
        PetService updatedService = petServiceRepository.save(existingService);

        // Chuyển đổi thực thể đã cập nhật sang DTO để trả về
        return petServiceMapper.toDTO(updatedService);
    }

    @Transactional
    public void deleteService(String id) {
        if (!petServiceRepository.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Pet service not found", "pet-service-s-01");
        }
        petServiceRepository.deleteById(id);
    }

    public Page<PetServiceResponse> findAll(Pageable pageable) {
        Page<PetService> petServices = petServiceRepository.findAll(pageable);
        return petServiceMapper.toPetServiceResponsePage(petServices);
    }

    public List<PetServiceResponse> getLatestServices(int count) {
        List<PetService> latestServices = petServiceRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"))
                .stream()
                .limit(count)
                .toList();
        return latestServices.stream()
                .map(petServiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<PetServiceResponse> findAllByDisabledFalse(Pageable pageable) {
        Page<PetService> petServices = petServiceRepository.findAllByDisabledFalse(pageable);
        return petServiceMapper.toPetServiceResponsePage(petServices);
    }
}
