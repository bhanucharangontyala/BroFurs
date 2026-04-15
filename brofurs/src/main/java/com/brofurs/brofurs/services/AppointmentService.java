package com.brofurs.brofurs.services;

import java.util.List;

import com.brofurs.brofurs.dto.AppointmentDto;
import com.brofurs.brofurs.entity.AppointmentRequest;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.enums.AppointmentStatus;

/**
 * AppointmentService
 *
 * Business logic for site visit (appointment) requests.
 *
 * book() → creates an AppointmentRequest from the product detail appointment
 * form, linked to the logged-in user findByUser() → user's own appointment list
 * (My Appointments page) findAll() → all appointments for the admin management
 * page updateStatus() → admin updates status (PENDING → CONFIRMED → COMPLETED /
 * CANCELLED) countAll() → total appointment count for the admin dashboard stat
 * card
 */
public interface AppointmentService {

	/**
	 * Book a site visit appointment for the given user. Product link is optional —
	 * may be a general visit request.
	 */
	AppointmentRequest book(AppointmentDto dto, User user);

	/**
	 * All appointment requests submitted by a specific user, newest first. Used for
	 * the "My Appointments" user dashboard page.
	 */
	List<AppointmentRequest> findByUser(User user);

	/**
	 * All appointment requests in the system, newest first. Used for the admin
	 * appointments management page.
	 */
	List<AppointmentRequest> findAll();

	/**
	 * Update the status of an appointment request. Status flow: PENDING → CONFIRMED
	 * → COMPLETED / CANCELLED
	 */
	AppointmentRequest updateStatus(Long id, AppointmentStatus status);

	/**
	 * Total appointment count — used for the admin dashboard stat card.
	 */
	long countAll();
}
