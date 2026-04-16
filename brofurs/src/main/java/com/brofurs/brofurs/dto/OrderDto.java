package com.brofurs.brofurs.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * OrderDto
 *
 * A lightweight DTO used for displaying order summaries in the user dashboard
 * and admin order list — not used for form binding.
 *
 * For form binding (placing an order), see OrderRequestDto.
 */
@Getter
@Setter
public class OrderDto {

	private Long id;
	private String orderNumber;
	private String customerName;
	private String customerPhone;
	private String deliveryAddress;
	private BigDecimal totalAmount;
	private String status;
	private String orderDate;
	private String notes;
}