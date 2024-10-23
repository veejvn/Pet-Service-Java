package com.group.pet_service.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] POST_PUBLIC_ENDPOINTS = {"/auth/**", "/", "/login"};
    private final String[] GET_PUBLIC_ENDPOINTS = {"/auth/**", "/", "/login"};

    @Autowired
    public JwtDecoder jwtDecoder;

    @Autowired
    public JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép tất cả các nguồn
        configuration.setAllowedOrigins(Arrays.asList("*"));

        // Cho phép tất cả các phương thức HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cho phép tất cả các headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Không cần cấu hình allowCredentials nếu cho phép mọi nguồn mà không cần gửi cookie
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L); // Giới hạn thời gian cache

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with your configuration
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, POST_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )

        );

        return httpSecurity.build();
    }



}