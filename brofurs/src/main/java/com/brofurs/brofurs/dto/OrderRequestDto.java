package com.brofurs.brofurs.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * OrderRequestDto
 *
 * Bound to the order placement form on the product detail page (the collapse
 * panel that opens when user clicks "Place Order").
 *
 * This DTO is processed by OrderController → OrderService.placeOrder().
 *
 * Fields explained:
 *
 * productId → hidden field, the product being ordered woodTypeId → selected
 * wood type from the radio buttons quantity → stepper input, minimum 1
 * customLength/Width/Height → optional custom dimensions (only if
 * product.customSizeAllowed) sizeUnit → "cm", "inch", or "feet"
 * customizationNotes → free text for special finish or design requirements
 * customerName → pre-filled from logged-in user's fullName customerPhone →
 * pre-filled from logged-in user's phone deliveryAddress → pre-filled from
 * logged-in user's address notes → general order notes / delivery instructions
 *
 * The logged-in User entity is resolved from the security context in
 * OrderController — it is NOT submitted via this form.
 */
@Getter
@Setter
public class OrderRequestDto {

	// ── Product & Wood Type ───────────────────────────────────────
	@NotNull(message = "Product is required")
	private Long productId;

	// Optional — customer may not select a wood type
	private Long woodTypeId;

	// ── Quantity ──────────────────────────────────────────────────
	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity = 1;

	// ── Custom Dimensions (optional) ──────────────────────────────
	// Only applicable when Product.customSizeAllowed = true
	private Double customLength;
	private Double customWidth;
	private Double customHeight;

	// Unit of measurement: "cm", "inch", "feet"
	@Size(max = 20)
	private String sizeUnit;

	// Free-text customization requirements
	@Size(max = 2000, message = "Customization notes must not exceed 2000 characters")
	private String customizationNotes;

	// ── Customer Contact Details ──────────────────────────────────
	// Pre-filled from logged-in user but editable on the form
	@Size(max = 150, message = "Name must not exceed 150 characters")
	private String customerName;

	@Size(max = 20, message = "Phone must not exceed 20 characters")
	private String customerPhone;

	@Size(max = 500, message = "Delivery address must not exceed 500 characters")
	private String deliveryAddress;

	// ── General Notes ─────────────────────────────────────────────
	@Size(max = 1000, message = "Notes must not exceed 1000 characters")
	private String notes;
}