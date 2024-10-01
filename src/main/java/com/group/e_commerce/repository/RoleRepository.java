package com.group.e_commerce.repository;

import com.group.e_commerce.enums.RoleEnum;
import com.group.e_commerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    }
