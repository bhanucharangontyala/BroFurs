package com.brofurs.brofurs.services;

import java.util.List;

import com.brofurs.brofurs.dto.AppointmentDto;
import com.brofurs.brofurs.entity.AppointmentRequest;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.enums.AppointmentStatus;

/**
 * AppointmentService
 * Business logic for site visit (appointment) requests.
 *
 * book() → creates an AppointmentRequest from the product detail appointment
 * form, linked to the logged-in user 
 * findByUser() → user's own appointment list(My Appointments page) 
 * findAll() → all appointments for the admin management page 
 * updateStatus() → admin updates status (PENDING → CONFIRMED → COMPLETED /
 * CANCELLED) 
 * countAll() → total appointment count for the admin dashboard stat
 * card
 */
public interface AppointmentService {

	AppointmentRequest book(AppointmentDto dto, User user);

	List<AppointmentRequest> findByUser(User user);

	List<AppointmentRequest> findAll();

	AppointmentRequest updateStatus(Long id, AppointmentStatus status);

	long countAll();
}
