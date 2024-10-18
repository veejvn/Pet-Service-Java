package com.group.pet_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.group.pet_service.dto.serviceItemDto.ServiceItemDTO;

public interface ServiceItemService {
    ServiceItemDTO createServiceItem(ServiceItemDTO serviceItemDTO);
    ServiceItemDTO getServiceItemById(String id);
    Page<ServiceItemDTO> getAllServiceItems(Pageable pageable);
    ServiceItemDTO updateServiceItem(String id, ServiceItemDTO serviceItemDTO);
    void deleteServiceItem(String id);
    Page<ServiceItemDTO> getServiceItemsByReceiptId(String receiptId, Pageable pageable);
    Page<ServiceItemDTO> getServiceItemsByStaffId(String staffId, Pageable pageable);
}