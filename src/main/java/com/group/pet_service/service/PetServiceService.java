package com.group.pet_service.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.group.pet_service.dto.pet_service.PetServiceEditRequest;
import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.PetServiceMapper;
import com.group.pet_service.model.PetService;
import org.springframework.dao.DataIntegrityViolationException;
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
    public void updateService(String id, PetServiceEditRequest request) throws IOException {

        PetService petService = petServiceRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Pet service not found"));

        petServiceMapper.updateEntityFromDTO(petService, request);
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            String imageCurrent = petService.getImage();
            uploadService.deleteFile(imageCurrent);
            String image = uploadService.uploadFile(request.getImageFile());
            petService.setImage(image);
        }
        petServiceRepository.save(petService);
    }

    @Transactional
    public void delete(String id) {
        try {
            petServiceRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException("Cannot delete species: It is being referenced by other records.");
        } catch (Exception e) {
            throw new AppException("An unexpected error occurred while deleting species.");
        }
    }

    public Page<PetServiceResponse> findAll(Pageable pageable) {
        Page<PetService> petServices = petServiceRepository.findAllByOrderByCreateAtDesc(pageable);
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

    public PetServiceEditRequest findById(String id) {
        PetService petService = petServiceRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Pet service not found")
        );
        return petServiceMapper.toPetServiceEditRequest(petService);
    }

    public Page<PetServiceResponse> findAllByDisabledFalse(Pageable pageable) {
        Page<PetService> petServices = petServiceRepository.findAllByDisabledFalseOrderByCreateAtDesc(pageable);
        return petServiceMapper.toPetServiceResponsePage(petServices);
    }
}
