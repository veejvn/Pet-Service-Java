package com.group.pet_service.repository;

import com.group.pet_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("Select u From User u Left Join Fetch u.roles Where u.username = :username")
    Optional<User> findByUsername(String username);
}
