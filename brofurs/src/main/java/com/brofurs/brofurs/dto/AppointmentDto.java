package com.brofurs.brofurs.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * AppointmentDto
 *
 * Bound to the appointment booking form on the product detail page. The
 * productId links the visit request to a specific product (optional — the
 * customer may just want a general store/site visit).
 *
 * The logged-in User is resolved from the security context in
 * AppointmentController — it is not submitted via the form.
 */
@Getter
@Setter
public class AppointmentDto {

	// Optional — links appointment to a specific product
	private Long productId;

	@NotNull(message = "Please select a preferred date")
	private LocalDate preferredDate;

	// Optional time preference
	private LocalTime preferredTime;

	@NotBlank(message = "Your name is required")
	@Size(max = 150, message = "Name must not exceed 150 characters")
	private String customerName;

	@Size(max = 20, message = "Phone number must not exceed 20 characters")
	private String phone;

	@Size(max = 500, message = "Address must not exceed 500 characters")
	private String address;

	@Size(max = 2000, message = "Message must not exceed 2000 characters")
	private String message;
}
