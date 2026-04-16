package com.brofurs.brofurs.dto;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * OrderItemDto
 *
 * Represents a single line item within an order.
 * Used internally when constructing order details — not bound directly to a form.
 *
 * Custom dimensions are all optional. They are only populated when
 * the product has customSizeAllowed = true and the user fills them in.
 */
@Getter
@Setter
public class OrderItemDto {

    private Long productId;

    private Long woodTypeId;

    // Number of units — minimum 1
    private Integer quantity = 1;

    // Price per unit at the time of ordering (basePrice + woodType adjustment)
    private BigDecimal unitPrice;

    // Optional custom dimensions
    private Double customLength;
    private Double customWidth;
    private Double customHeight;

    // Unit of measurement: "cm", "inch", or "feet"
    private String sizeUnit;

    // Free-text notes about customization requirements
    private String customizationNotes;
}