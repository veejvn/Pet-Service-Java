package com.group.pet_service.repository;

import com.group.pet_service.model.PetService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetServiceRepository extends JpaRepository<PetService, String> {
    Optional<PetService> findByName(String s);
}
