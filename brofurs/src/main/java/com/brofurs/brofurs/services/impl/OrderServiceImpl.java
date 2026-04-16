package com.brofurs.brofurs.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brofurs.brofurs.dto.OrderRequestDto;
import com.brofurs.brofurs.entity.Order;
import com.brofurs.brofurs.entity.OrderItem;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.entity.WoodType;
import com.brofurs.brofurs.enums.OrderStatus;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.repository.OrderRepository;
import com.brofurs.brofurs.repository.ProductRepository;
import com.brofurs.brofurs.repository.WoodTypeRepository;
import com.brofurs.brofurs.services.OrderService;
import com.brofurs.brofurs.services.ProductService;

/**
 * OrderServiceImpl
 *
 * Business logic for order placement and management.
 *
 * placeOrder() flow:
 *  1. Resolve product and optional wood type from the DTO 
 *  2. Calculate unit price (via ProductService.calculatePrice) 
 *  3. Generate a unique order number: FC-{yyyyMMddHHmm}-{4-digit-random} 
 *  4. Build the Order header (customer details, total amount, status = NEW) 
 *  5. Build the OrderItem (snapshot of product name, wood type, dimensions) 
 *  6. Persist and return the saved Order
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final WoodTypeRepository woodTypeRepository;
	private final ProductService productService;

	public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
			WoodTypeRepository woodTypeRepository, ProductService productService) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.woodTypeRepository = woodTypeRepository;
		this.productService = productService;
	}

	// ── Place Order ───────────────────────────────────────────────

	@Override
	public Order placeOrder(OrderRequestDto dto, User user) {
		// 1. Resolve product
		Product product = productRepository.findById(dto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));

		// 2. Resolve optional wood type
		WoodType woodType = null;
		if (dto.getWoodTypeId() != null) {
			woodType = woodTypeRepository.findById(dto.getWoodTypeId()).orElse(null);
		}

		// 3. Calculate unit price based on product + wood type
		BigDecimal unitPrice = productService.calculatePrice(product.getId(),
				woodType != null ? woodType.getId() : null);

		int quantity = dto.getQuantity() != null ? dto.getQuantity() : 1;
		BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));

		// 4. Build Order header
		Order order = new Order();
		order.setOrderNumber(generateOrderNumber());
		order.setUser(user);
		order.setStatus(OrderStatus.NEW);
		order.setTotalAmount(totalAmount);

		// Use form values if provided, otherwise fall back to user profile
		order.setCustomerName(isBlank(dto.getCustomerName()) ? user.getFullName() : dto.getCustomerName());
		order.setCustomerPhone(isBlank(dto.getCustomerPhone()) ? user.getPhone() : dto.getCustomerPhone());
		order.setDeliveryAddress(isBlank(dto.getDeliveryAddress()) ? user.getAddress() : dto.getDeliveryAddress());
		order.setNotes(dto.getNotes());

		// 5. Build OrderItem (snapshot of product at time of ordering)
		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setProduct(product);
		item.setProductNameSnapshot(product.getName());
		item.setSelectedWoodType(woodType);
		item.setUnitPrice(unitPrice);
		item.setQuantity(quantity);

		// Optional custom dimensions
		item.setCustomLength(dto.getCustomLength());
		item.setCustomWidth(dto.getCustomWidth());
		item.setCustomHeight(dto.getCustomHeight());
		item.setSizeUnit(dto.getSizeUnit());
		item.setCustomizationNotes(dto.getCustomizationNotes());

		order.getOrderItems().add(item);

		// 6. Persist (cascades to OrderItem)
		return orderRepository.save(order);
	}

	// ── Read ──────────────────────────────────────────────────────

	@Override
	@Transactional(readOnly = true)
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findByUser(User user) {
		return orderRepository.findByUserIdOrderByOrderDateDesc(user.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findAll() {
		return orderRepository.findAllByOrderByOrderDateDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findRecent(int count) {
		return orderRepository.findTop5ByOrderByOrderDateDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public long countByStatus(OrderStatus status) {
		return orderRepository.countByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public long countTotal() {
		return orderRepository.count();
	}

	// ── Update Status ─────────────────────────────────────────────

	@Override
	public Order updateStatus(Long id, OrderStatus status) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
		order.setStatus(status);
		return orderRepository.save(order);
	}

	/**
	 * Generate a unique order number in the format:
	 * FC-{yyyyMMddHHmm}-{4-digit-random} Example: FC-202401151430-7823
	 */
	private String generateOrderNumber() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		int random = ThreadLocalRandom.current().nextInt(1000, 9999);
		return "BF-" + timestamp + "-" + random;
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}
}