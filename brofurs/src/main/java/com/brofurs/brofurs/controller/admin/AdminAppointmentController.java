package com.brofurs.brofurs.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brofurs.brofurs.enums.AppointmentStatus;
import com.brofurs.brofurs.services.AppointmentService;

/**
 * AdminAppointmentController
 *
 * View and manage all customer site visit requests. Requires ADMIN role.
 *
 * GET /admin/appointments → list all appointment requests (newest first) POST
 * /admin/appointments/{id}/status → update appointment status inline
 *
 * Appointment statuses: PENDING → CONFIRMED → COMPLETED / CANCELLED
 */
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

	private final AppointmentService appointmentService;

	public AdminAppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("appointments", appointmentService.findAll());
		model.addAttribute("statuses", AppointmentStatus.values());
		return "admin/appointments/list";
	}

	@PostMapping("/{id}/status")
	public String updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status,
			RedirectAttributes attrs) {
		try {
			appointmentService.updateStatus(id, status);
			attrs.addFlashAttribute("successMessage", "Appointment status updated to: " + status.getDisplayName());
		} catch (Exception e) {
			attrs.addFlashAttribute("errorMessage", "Failed to update status: " + e.getMessage());
		}
		return "redirect:/admin/appointments";
	}
}