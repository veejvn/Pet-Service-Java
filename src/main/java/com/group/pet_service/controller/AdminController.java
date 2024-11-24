package com.group.pet_service.controller;

import com.group.pet_service.dto.auth.AdminLoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    @GetMapping("/admin/login")
    public String showLoginForm(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && session != null) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("loginRequest", new AdminLoginRequest());
        return "Login";
    }
}
