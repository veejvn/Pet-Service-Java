package com.group.pet_service.configuration;


import com.group.pet_service.enums.Role;
import com.group.pet_service.model.User;
import com.group.pet_service.repository.UserRepository;
import com.group.pet_service.util.PasswordUtil;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    @Value("${app.admin.email}")
    @NonFinal
    String ADMIN_EMAIL;

    @Value("${app.admin.password}")
    @NonFinal
    String ADMIN_PASSWORD;

    UserRepository userRepository;
    PasswordUtil passwordUtil;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            boolean isExistedAdmin = userRepository.existsByEmail(ADMIN_EMAIL);
            if (isExistedAdmin) return;

            Set<Role> roles = EnumSet.allOf(Role.class);

            User admin = User.builder()
                    .email(ADMIN_EMAIL)
                    .username("admin")
                    .password(passwordUtil.encodePassword(ADMIN_PASSWORD))
                    .roles(roles)
                    .build();
            userRepository.save(admin);
        };
    }
}