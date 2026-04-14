package com.brofurs.brofurs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.Order;
import com.brofurs.brofurs.enums.OrderStatus;

/**
 * Repository for Order entity.
 *
 * Supports two main access patterns: 1. User-facing: "My Orders" page — only
 * the logged-in user's orders 2. Admin-facing: all orders with status filters
 * for the dashboard and order list
 *
 * Orders always sorted by orderDate descending (newest first).
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * All orders placed by a specific user, newest first. Used for the "My Orders"
	 * page in the user dashboard.
	 */
	List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

	/**
	 * All orders in the system, newest first. Used for the admin orders list page.
	 */
	List<Order> findAllByOrderByOrderDateDesc();

	/**
	 * The 5 most recent orders across all users. Used for the "Recent Orders" table
	 * on the admin dashboard.
	 */
	List<Order> findTop5ByOrderByOrderDateDesc();

	/**
	 * Count orders by status — used for dashboard stat cards. Example:
	 * countByStatus(OrderStatus.NEW) shows pending order count.
	 */
	long countByStatus(OrderStatus status);
}
