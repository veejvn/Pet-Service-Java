package com.group.e_commerce.repository;

import com.group.e_commerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);

    @Query("Select u From User u Left Join Fetch u.roles Where u.username = :username")
    Optional<User> findByUsername(String username);
}
