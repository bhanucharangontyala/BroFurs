package com.brofurs.brofurs.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brofurs.brofurs.entity.Order;
import com.brofurs.brofurs.enums.OrderStatus;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.services.OrderService;

/**
 * AdminOrderController
 *
 * View and manage all customer orders. Requires ADMIN role.
 *
 * GET  /admin/orders             → list all orders (newest first)
 * GET  /admin/orders/{id}        → order detail with customer info, items, dimensions
 * POST /admin/orders/{id}/status → update order status
 *
 * Order statuses: NEW → CONFIRMED → IN_PRODUCTION → READY → DELIVERED / CANCELLED
 */
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/details";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status,
                               RedirectAttributes attrs) {
        try {
            orderService.updateStatus(id, status);
            attrs.addFlashAttribute("successMessage",
                "Order status updated to: " + status.getDisplayName());
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage",
                "Failed to update status: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }
}