package com.group.pet_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group.pet_service.model.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, String> {
    Page<ServiceItem> findByReceiptId(String receiptId, Pageable pageable);
    Page<ServiceItem> findByStaffId(String staffId, Pageable pageable);
}