package com.brofurs.brofurs.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product;

	// Snapshot of product name at time of order
	@Column(nullable = false, length = 200)
	private String productNameSnapshot;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wood_type_id")
	private WoodType selectedWoodType;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal unitPrice;

	@Column(nullable = false)
	private Integer quantity = 1;

	// Optional custom dimensions
	private Double customLength;
	private Double customWidth;
	private Double customHeight;

	@Column(length = 20)
	private String sizeUnit; // cm, inch, feet

	@Column(columnDefinition = "TEXT")
	private String customizationNotes;
}