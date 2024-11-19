package com.group.pet_service.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.group.pet_service.dto.response.ServiceResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.model.ServiceImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.group.pet_service.dto.request.ServiceRequest;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.Service;
import com.group.pet_service.repository.ServiceRepository;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public Page<ServiceResponse> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(serviceMapper::toDTO);
    }

    public ServiceResponse getServiceById(String id) {
        return serviceRepository.findById(id)
                .map(serviceMapper::toDTO)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "service-s-01"));
    }

    @Transactional
    public ServiceResponse createService(ServiceRequest serviceRequest) {
        Service service = serviceMapper.toEntity(serviceRequest);
        service.setCreateAt(Timestamp.from(Instant.now()));
        Set<ServiceImage> images = new HashSet<>();
        for(String imageUrl : serviceRequest.getImages()){
            ServiceImage image = ServiceImage.builder()
                    .url(imageUrl)
                    .service(service)
                    .build();
            images.add(image);
        }
        service.setImages(images);
        service = serviceRepository.save(service);
        return serviceMapper.toDTO(service);
    }

    @Transactional
    public ServiceResponse updateService(String id, ServiceRequest serviceRequest) {
        // Tìm dịch vụ theo ID
        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "service-s-01"));

        // Cập nhật thông tin từ ServiceDTO vào thực thể đã tồn tại
        serviceMapper.updateEntityFromDTO(serviceRequest, existingService);

        // Lưu và cập nhật dịch vụ
        Service updatedService = serviceRepository.save(existingService);

        // Chuyển đổi thực thể đã cập nhật sang DTO để trả về
        return serviceMapper.toDTO(updatedService);
    }

    @Transactional
    public void deleteService(String id) {
        if (!serviceRepository.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Service not found", "service-s-01");
        }
        serviceRepository.deleteById(id);
    }

    public List<Service> serviceList (){
        return serviceRepository.findAll();
    }

	public List<ServiceResponse> getLatestServices(int count) {
        List<Service> latestServices = serviceRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"))
                .stream()
                .limit(count)
                .toList();
        return latestServices.stream()
                .map(serviceMapper::toDTO)
                .collect(Collectors.toList());
    }
}
