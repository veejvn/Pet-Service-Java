package com.group.pet_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.group.pet_service.dto.request.ServiceItemRequest;

public interface ServiceItemService {
    ServiceItemRequest createServiceItem(ServiceItemRequest serviceItemRequest);
    ServiceItemRequest getServiceItemById(String id);
    Page<ServiceItemRequest> getAllServiceItems(Pageable pageable);
    ServiceItemRequest updateServiceItem(String id, ServiceItemRequest serviceItemRequest);
    void deleteServiceItem(String id);
    Page<ServiceItemRequest> getServiceItemsByReceiptId(String receiptId, Pageable pageable);
    Page<ServiceItemRequest> getServiceItemsByStaffId(String staffId, Pageable pageable);
}