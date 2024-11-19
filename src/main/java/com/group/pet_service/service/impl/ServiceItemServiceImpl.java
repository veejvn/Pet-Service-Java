package com.group.pet_service.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.group.pet_service.dto.request.ServiceItemRequest;
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
    public ServiceItemRequest createServiceItem(ServiceItemRequest serviceItemRequest) {
        ServiceItem serviceItem = serviceItemMapper.toEntity(serviceItemRequest);
        ServiceItem savedItem = serviceItemRepository.save(serviceItem);
        return serviceItemMapper.toDTO(savedItem);
    }

    @Override
    public ServiceItemRequest getServiceItemById(String id) {
        Optional<ServiceItem> serviceItem = serviceItemRepository.findById(id);
        return serviceItem.map(serviceItemMapper::toDTO).orElse(null);
    }

    @Override
    public Page<ServiceItemRequest> getAllServiceItems(Pageable pageable) {
        Page<ServiceItem> serviceItems = serviceItemRepository.findAll(pageable);
        return serviceItems.map(serviceItemMapper::toDTO);
    }

    @Override
    public ServiceItemRequest updateServiceItem(String id, ServiceItemRequest serviceItemRequest) {
        if (serviceItemRepository.existsById(id)) {
            ServiceItem serviceItem = serviceItemMapper.toEntity(serviceItemRequest);
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
    public Page<ServiceItemRequest> getServiceItemsByReceiptId(String receiptId, Pageable pageable) {
        Page<ServiceItem> serviceItems = serviceItemRepository.findByReceiptId(receiptId, pageable);
        return serviceItems.map(serviceItemMapper::toDTO);
    }

    @Override
    public Page<ServiceItemRequest> getServiceItemsByStaffId(String staffId, Pageable pageable) {
        Page<ServiceItem> serviceItems = serviceItemRepository.findByStaffId(staffId, pageable);
        return serviceItems.map(serviceItemMapper::toDTO);
    }
}