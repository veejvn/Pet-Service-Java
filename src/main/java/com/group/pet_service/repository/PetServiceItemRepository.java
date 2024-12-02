package com.group.pet_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group.pet_service.model.PetServiceItem;

import java.util.Optional;

public interface PetServiceItemRepository extends JpaRepository<PetServiceItem, String> {
    Optional<PetServiceItem> findByIdAndStaffId(String id, String staffId);
}