package com.brofurs.brofurs.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.services.AppointmentService;
import com.brofurs.brofurs.services.OrderService;
import com.brofurs.brofurs.services.UserService;

/**
 * UserDashboardController
 *
 * Handles all pages under /user/** — requires USER role (enforced by SecurityConfig).
 *
 * GET /user/dashboard    → overview with profile, order count, appointment count
 * GET /user/orders       → full order history for the logged-in user
 * GET /user/appointments → all appointment requests for the logged-in user
 */
@Controller
@RequestMapping("/user")
public class UserDashboardController {

    private final UserService userService;
    private final OrderService orderService;
    private final AppointmentService appointmentService;

    public UserDashboardController(UserService userService,
                                   OrderService orderService,
                                   AppointmentService appointmentService) {
        this.userService = userService;
        this.orderService = orderService;
        this.appointmentService = appointmentService;
    }

    // Resolve the authenticated User entity from the security principal
    private User resolveUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));
    }

    // ── Dashboard Overview ────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = resolveUser(userDetails);

        model.addAttribute("currentUser", user);
        model.addAttribute("totalOrders", orderService.findByUser(user).size());
        model.addAttribute("totalAppointments", appointmentService.findByUser(user).size());

        return "user/dashboard";
    }

    // ── My Orders ─────────────────────────────────────
    @GetMapping("/orders")
    public String orders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = resolveUser(userDetails);

        model.addAttribute("currentUser", user);
        model.addAttribute("orders", orderService.findByUser(user));

        return "user/orders";
    }

    // ── My Appointments ───────────────────────────────
    @GetMapping("/appointments")
    public String appointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = resolveUser(userDetails);

        model.addAttribute("currentUser", user);
        model.addAttribute("appointments", appointmentService.findByUser(user));

        return "user/appointments";
    }
}