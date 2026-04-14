package com.brofurs.brofurs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.AppointmentRequest;

/**
 * Repository for AppointmentRequest entity.
 *
 * Appointment requests are site visit bookings made by logged-in users from the
 * product detail page. They can optionally be linked to a product (if the
 * customer is interested in a specific item) or be a general visit.
 *
 * Access patterns: - User side: show only the logged-in user's appointments -
 * Admin side: show all appointments with status management
 */
public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

	/**
	 * All appointment requests submitted by a specific user, newest first. Used for
	 * the "My Appointments" page in the user dashboard.
	 */
	List<AppointmentRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

	/**
	 * All appointment requests in the system, newest first. Used for the admin
	 * appointments management page.
	 */
	List<AppointmentRequest> findAllByOrderByCreatedAtDesc();
}
