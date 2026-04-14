package com.brofurs.brofurs.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;

import com.brofurs.brofurs.enums.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointment_requests")
@Getter
@Setter
@NoArgsConstructor
public class AppointmentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product; // nullable - may be a general visit

	private LocalDate preferredDate;
	private LocalTime preferredTime;

	@Column(nullable = false, length = 150)
	private String customerName;

	@Column(length = 20)
	private String phone;

	@Column(length = 500)
	private String address;

	@Column(columnDefinition = "TEXT")
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private AppointmentStatus status = AppointmentStatus.PENDING;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
}