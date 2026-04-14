package com.brofurs.brofurs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.OrderItem;

/**
 * Repository for OrderItem entity.
 *
 * Each Order can contain one or more OrderItems. Each item captures a snapshot
 * of the product name, selected wood type, unit price, quantity, and optional
 * custom dimensions at the time of ordering.
 *
 * OrderItems are loaded eagerly via the Order entity in most cases, but this
 * repository allows direct queries when needed (e.g. reporting).
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	/**
	 * All items belonging to a specific order. Normally accessed via
	 * order.getOrderItems(), but available for direct queries if needed.
	 */
	List<OrderItem> findByOrderId(Long orderId);
}
