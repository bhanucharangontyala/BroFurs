package com.brofurs.brofurs.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brofurs.brofurs.services.UserService;

/**
 * AdminUserController
 *
 * Read-only view of all registered user accounts. Requires ADMIN role.
 *
 * GET /admin/users → list all registered users with their roles and status
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";
    }
}
