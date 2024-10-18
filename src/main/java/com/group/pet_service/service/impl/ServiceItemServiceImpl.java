package com.group.pet_service.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.group.pet_service.dto.serviceItemDto.ServiceItemDTO;
import com.group.pet_service.model.ServiceItem;
import com.group.pet_service.repository.ServiceItemRepository;
import com.group.pet_service.service.ServiceItemService;
import com.group.pet_service.mapper.ServiceItemMapper;

@Service
public class ServiceItemServiceImpl implements ServiceItemService {
    @Autowired
    private ServiceItemRepository serviceItemRepository;
    
    @Autowired
    private ServiceItemMapper serviceItemMapper;

    @Override
    public ServiceItemDTO createServiceItem(ServiceItemDTO serviceItemDTO) {
        ServiceItem serviceItem = serviceItemMapper.toEntity(serviceItemDTO);
        ServiceItem savedItem = serviceItemRepository.save(serviceItem);
        return serviceItemMapper.toDTO(savedItem);
    }

    @Override
    public ServiceItemDTO getServiceItemById(String id) {
        Optional<ServiceItem> serviceItem = serviceItemRepository.findById(id);
        return serviceItem.map(serviceItemMapper::toDTO).orElse(null);
    }

    @Override
    public Page<ServiceItemDTO> getAllServiceItems(Pageable pageable) {
        Page<ServiceItem> serviceItems = serviceItemRepository.findAll(pageable);
        return serviceItems.map(serviceItemMapper::toDTO);
    }

    @Override
    public ServiceItemDTO updateServiceItem(String id, ServiceItemDTO serviceItemDTO) {
        if (serviceItemRepository.existsById(id)) {
            ServiceItem serviceItem = serviceItemMapper.toEntity(serviceItemDTO);
            serviceItem.setId(id);
            ServiceItem updatedItem = serviceItemRepository.save(serviceItem);
            return serviceItemMapper.toDTO(updatedItem);
        }
        return null;
    }

    @Override
    public void deleteServiceItem(String id) {
        serviceItemRepository.deleteById(id);
    }

    @Override
    public Page<ServiceItemDTO> getServiceItemsByReceiptId(String receiptId, Pageable pageable) {
        Page<ServiceItem> serviceItems = serviceItemRepository.findByReceiptId(receiptId, pageable);
        return serviceItems.map(serviceItemMapper::toDTO);
    }

    @Override
    public Page<ServiceItemDTO> getServiceItemsByStaffId(String staffId, Pageable pageable) {
        Page<ServiceItem> serviceItems = serviceItemRepository.findByStaffId(staffId, pageable);
        return serviceItems.map(serviceItemMapper::toDTO);
    }
}