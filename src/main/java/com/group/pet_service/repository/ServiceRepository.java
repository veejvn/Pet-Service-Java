package com.group.pet_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group.pet_service.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
}
