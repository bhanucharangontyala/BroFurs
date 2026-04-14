package com.brofurs.brofurs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product_wood_prices", uniqueConstraints = @UniqueConstraint(columnNames = { "product_id",
		"wood_type_id" }))
@Getter
@Setter
@NoArgsConstructor
public class ProductWoodPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wood_type_id", nullable = false)
	private WoodType woodType;

	// Added to base price
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal priceAdjustment = BigDecimal.ZERO;

	// If set, this overrides the calculation entirely
	@Column(precision = 12, scale = 2)
	private BigDecimal finalPriceOverride;

	@Column(nullable = false)
	private boolean active = true;

	// Returns the effective price given base price
	public BigDecimal calculateFinalPrice(BigDecimal basePrice) {
		if (finalPriceOverride != null) {
			return finalPriceOverride;
		}
		return basePrice.add(priceAdjustment);
	}
}