package com.group.pet_service.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordUtil {
    PasswordEncoder passwordEncoder;
    public String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }
    public boolean checkPassword(String rawPassword, String encodePassword){
        return passwordEncoder.matches(rawPassword, encodePassword);
    }
}
