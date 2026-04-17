package com.brofurs.brofurs.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brofurs.brofurs.dto.AppointmentDto;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.services.AppointmentService;
import com.brofurs.brofurs.services.UserService;

import jakarta.validation.Valid;

/**
 * AppointmentController
 *
 * Handles site visit (appointment) booking from the product detail page.
 * Requires USER role (enforced by SecurityConfig).
 *
 * POST /appointment/book → saves the AppointmentRequest linked to the user
 *                          and optionally to a product, then redirects to
 *                          the user's appointments page with a success message.
 */
@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AppointmentController(AppointmentService appointmentService,
                                 UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @PostMapping("/book")
    public String book(@Valid @ModelAttribute AppointmentDto dto,
                       BindingResult result,
                       @AuthenticationPrincipal UserDetails userDetails,
                       RedirectAttributes attrs) {

        // On validation error redirect back to products
        if (result.hasErrors()) {
            attrs.addFlashAttribute("errorMessage", "Please fill in all required appointment fields.");
            return "redirect:/products";
        }

        // Resolve the logged-in user
        User user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));

        appointmentService.book(dto, user);

        attrs.addFlashAttribute("successMessage",
            "Appointment request submitted! Our team will confirm your visit date shortly.");
        return "redirect:/user/appointments";
    }
}