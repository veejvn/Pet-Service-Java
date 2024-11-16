package com.group.pet_service.util;

import com.group.pet_service.exception.AppException;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public String getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AppException(HttpStatus.UNAUTHORIZED, "User isn't authenticated", "auth-e-00");
        }
        return authentication.getName();
    }

    public User getUser(){
        return userRepository.findById(this.getUserId()).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-01")
        );
    }

}
