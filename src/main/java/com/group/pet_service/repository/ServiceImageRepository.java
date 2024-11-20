package com.group.pet_service.repository;

import com.group.pet_service.model.PetService;
import com.group.pet_service.model.ServiceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceImageRepository extends JpaRepository<ServiceImage, String> {
}
