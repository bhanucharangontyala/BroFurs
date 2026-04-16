package com.brofurs.brofurs.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brofurs.brofurs.dto.AppointmentDto;
import com.brofurs.brofurs.entity.AppointmentRequest;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.enums.AppointmentStatus;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.repository.AppointmentRequestRepository;
import com.brofurs.brofurs.repository.ProductRepository;
import com.brofurs.brofurs.services.AppointmentService;

/**
 * AppointmentServiceImpl
 *
 * Business logic for site visit (appointment) booking.
 *
 * book() flow: 1. Build AppointmentRequest from the DTO 2. Link to the
 * logged-in User 3. Optionally link to a specific Product (can be null for
 * general visits) 4. Default status = PENDING 5. Persist and return
 *
 * Admin can then update status to CONFIRMED → COMPLETED / CANCELLED.
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRequestRepository appointmentRepository;
	private final ProductRepository productRepository;

	public AppointmentServiceImpl(AppointmentRequestRepository appointmentRepository,
			ProductRepository productRepository) {
		this.appointmentRepository = appointmentRepository;
		this.productRepository = productRepository;
	}

	// ── Book ──────────────────────────────────────────────────────

	@Override
	public AppointmentRequest book(AppointmentDto dto, User user) {
		AppointmentRequest appointment = new AppointmentRequest();

		appointment.setUser(user);
		appointment.setCustomerName(dto.getCustomerName());
		appointment.setPhone(dto.getPhone());
		appointment.setAddress(dto.getAddress());
		appointment.setPreferredDate(dto.getPreferredDate());
		appointment.setPreferredTime(dto.getPreferredTime());
		appointment.setMessage(dto.getMessage());
		appointment.setStatus(AppointmentStatus.PENDING);

		// Optionally link to a product — null means general store/site visit
		if (dto.getProductId() != null) {
			Product product = productRepository.findById(dto.getProductId()).orElse(null);
			appointment.setProduct(product);
		}

		return appointmentRepository.save(appointment);
	}

	// ── Read ──────────────────────────────────────────────────────

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentRequest> findByUser(User user) {
		return appointmentRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentRequest> findAll() {
		return appointmentRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public long countAll() {
		return appointmentRepository.count();
	}

	// ── Update Status ─────────────────────────────────────────────

	@Override
	public AppointmentRequest updateStatus(Long id, AppointmentStatus status) {
		AppointmentRequest appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AppointmentRequest", id));
		appointment.setStatus(status);
		return appointmentRepository.save(appointment);
	}
}