package com.group.pet_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group.pet_service.model.PetServiceItem;

public interface ServiceItemRepository extends JpaRepository<PetServiceItem, String> {
    Page<PetServiceItem> findByReceiptId(String receiptId, Pageable pageable);

    Page<PetServiceItem> findByStaffId(String staffId, Pageable pageable);
}