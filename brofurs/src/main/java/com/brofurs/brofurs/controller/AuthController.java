package com.brofurs.brofurs.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brofurs.brofurs.dto.UserRegistrationDto;
import com.brofurs.brofurs.services.UserService;

import jakarta.validation.Valid;

/**
 * AuthController
 *
 * Handles user registration and the login page.
 * The actual login form POST is processed by Spring Security — we only
 * render the login page here (Spring Security handles /login POST).
 *
 * After successful registration the user is redirected to /login
 * with a flash success message.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ── Login ────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // ── Registration ─────────────────────────────────
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDto") UserRegistrationDto dto,
                           BindingResult result,
                           RedirectAttributes attrs) {

        // Return to form if JSR-303 validation failed
        if (result.hasErrors()) {
            return "auth/register";
        }

        // Passwords must match
        if (!dto.passwordsMatch()) {
            result.rejectValue("confirmPassword", "error.userDto", "Passwords do not match");
            return "auth/register";
        }

        // Email must be unique
        if (userService.emailExists(dto.getEmail())) {
            result.rejectValue("email", "error.userDto", "This email is already registered");
            return "auth/register";
        }

        userService.registerUser(dto);
        attrs.addFlashAttribute("successMessage", "Account created successfully! Please login.");
        return "redirect:/login";
    }
}