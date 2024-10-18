package com.group.pet_service.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.group.pet_service.dto.serviceDto.ServiceDTO;
import com.group.pet_service.exception.ResourceNotFoundException;
import com.group.pet_service.mapper.ServiceMapper;
import com.group.pet_service.model.Service;
import com.group.pet_service.repository.ServiceRepository;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public Page<ServiceDTO> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(serviceMapper::toDTO);
    }

    public ServiceDTO getServiceById(String id) {
        return serviceRepository.findById(id)
                .map(serviceMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
    }

    @Transactional
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Service service = serviceMapper.toEntity(serviceDTO);
        service.setCreateAt(Timestamp.from(Instant.now()));
        service = serviceRepository.save(service);
        return serviceMapper.toDTO(service);
    }

    @Transactional
    public ServiceDTO updateService(String id, ServiceDTO serviceDTO) {
        // Tìm dịch vụ theo ID
        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        // Cập nhật thông tin từ ServiceDTO vào thực thể đã tồn tại
        serviceMapper.updateEntityFromDTO(serviceDTO, existingService);

        // Lưu và cập nhật dịch vụ
        Service updatedService = serviceRepository.save(existingService);

        // Chuyển đổi thực thể đã cập nhật sang DTO để trả về
        return serviceMapper.toDTO(updatedService);
    }

    @Transactional
    public void deleteService(String id) {
        if (!serviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service not found with id: " + id);
        }
        serviceRepository.deleteById(id);
    }

    public List<Service> serviceList (){
        return serviceRepository.findAll();
    }

	public List<ServiceDTO> getLatestServices(int count) {
        List<Service> latestServices = serviceRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"))
                .stream()
                .limit(count)
                .collect(Collectors.toList());
        return latestServices.stream()
                .map(serviceMapper::toDTO)
                .collect(Collectors.toList());
    }
}
