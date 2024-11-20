package com.group.pet_service.repository;

import com.group.pet_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, String> {
    Optional<Pet> findByName(String s);
}
