package com.brofurs.brofurs.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brofurs.brofurs.enums.OrderStatus;
import com.brofurs.brofurs.services.AppointmentService;
import com.brofurs.brofurs.services.OrderService;
import com.brofurs.brofurs.services.ProductService;

/**
 * AdminDashboardController
 *
 * Renders the admin dashboard at /admin and /admin/dashboard. Requires ADMIN
 * role (enforced by SecurityConfig).
 *
 * Populates four stat cards: totalProducts → count of active products
 * totalOrders → count of all orders pendingOrders → count of orders with status
 * NEW totalAppointments → count of all appointment requests
 *
 * Also loads the 5 most recent orders for the dashboard table.
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

	private final ProductService productService;
	private final OrderService orderService;
	private final AppointmentService appointmentService;

	public AdminDashboardController(ProductService productService, OrderService orderService,
			AppointmentService appointmentService) {
		this.productService = productService;
		this.orderService = orderService;
		this.appointmentService = appointmentService;
	}

	@GetMapping({ "/", "/dashboard" })
	public String dashboard(Model model) {
		model.addAttribute("totalProducts", productService.countActive());
		model.addAttribute("totalOrders", orderService.countTotal());
		model.addAttribute("pendingOrders", orderService.countByStatus(OrderStatus.NEW));
		model.addAttribute("totalAppointments", appointmentService.countAll());
		model.addAttribute("recentOrders", orderService.findRecent(5));
		return "admin/dashboard";
	}
}
