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
 * orders for the admin order list 
 * findRecent() → last N orders for the admin dashboard table 
 * updateStatus() → admin changes order status 
 * countByStatus() → dashboard stat card (pending orders count) 
 * countTotal() → dashboard stat card (total orders count)
 */
public interface OrderService {

	Order placeOrder(OrderRequestDto dto, User user);

	Optional<Order> findById(Long id);

	List<Order> findByUser(User user);

	List<Order> findAll();

	List<Order> findRecent(int count);

	Order updateStatus(Long id, OrderStatus status);

	long countByStatus(OrderStatus status);

	long countTotal();
}
