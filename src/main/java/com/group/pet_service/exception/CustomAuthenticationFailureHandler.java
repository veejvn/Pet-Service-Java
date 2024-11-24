package com.group.pet_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String errorMessage = "Invalid email or password";

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Email account not found";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid password";
        }

        response.sendRedirect("/admin/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }
}
