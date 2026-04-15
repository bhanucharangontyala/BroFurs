package com.brofurs.brofurs.services;

import java.util.List;
import java.util.Optional;

import com.brofurs.brofurs.dto.OrderRequestDto;
import com.brofurs.brofurs.entity.Order;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.enums.OrderStatus;

/**
 * OrderService
 *
 * Business logic for customer orders.
 *
 * placeOrder() → creates an Order + OrderItem from the product detail form,
 * calculates final price based on selected wood type, generates a unique order
 * number (FC-yyyyMMddHHmm-XXXX)
 *
 * findByUser() → user's own order history (My Orders page) findAll() → all
 * orders for the admin order list findRecent() → last N orders for the admin
 * dashboard table updateStatus() → admin changes order status countByStatus() →
 * dashboard stat card (pending orders count) countTotal() → dashboard stat card
 * (total orders count)
 */
public interface OrderService {

	/**
	 * Place a new order on behalf of a logged-in user. Calculates unit price,
	 * builds Order + OrderItem, saves both.
	 */
	Order placeOrder(OrderRequestDto dto, User user);

	/**
	 * Find a single order by ID — used by the admin order detail page.
	 */
	Optional<Order> findById(Long id);

	/**
	 * All orders placed by a specific user, newest first. Used for the "My Orders"
	 * user dashboard page.
	 */
	List<Order> findByUser(User user);

	/**
	 * All orders in the system, newest first. Used for the admin orders list.
	 */
	List<Order> findAll();

	/**
	 * The N most recent orders — used for the admin dashboard table.
	 */
	List<Order> findRecent(int count);

	/**
	 * Update the status of an order. Status flow: NEW → CONFIRMED → IN_PRODUCTION →
	 * READY → DELIVERED / CANCELLED
	 */
	Order updateStatus(Long id, OrderStatus status);

	/**
	 * Count orders by a specific status — used for the dashboard "Pending" stat
	 * card.
	 */
	long countByStatus(OrderStatus status);

	/**
	 * Total order count — used for the dashboard "Total Orders" stat card.
	 */
	long countTotal();
}
