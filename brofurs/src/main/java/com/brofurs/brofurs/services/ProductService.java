package com.brofurs.brofurs.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brofurs.brofurs.dto.ProductDto;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.ProductWoodPrice;

/**
 * ProductService
 *
 * Core business logic for the furniture product catalogue.
 *
 * Public-facing methods: findFeatured() → latest 8 active products for the home
 * page searchProducts() → paginated filtered listing for the products page
 * findBySlug() → single product for the detail page getWoodPrices() → list of
 * wood pricing rows for the radio selector getWoodPriceMap() → Map<woodTypeId,
 * ProductWoodPrice> injected as JSON for JS calculatePrice() → final price for
 * a product + wood type combination
 *
 * Admin methods: findAllActive() → full list for the admin product table save()
 * / update() → create / edit with image upload and wood pricing delete() → hard
 * delete with image file cleanup countActive() → dashboard stat card
 */
public interface ProductService {

	/** Latest 8 active products for the home page featured section. */
	List<Product> findFeatured();

	/** All active products newest-first — for the admin product list. */
	List<Product> findAllActive();

	/** Find a single product by database ID. */
	Optional<Product> findById(Long id);

	/** Find a single product by URL slug — used on the detail page. */
	Optional<Product> findBySlugWithImages(String slug);

	/**
	 * Paginated product search combining optional category and name filters. Either
	 * filter may be null to skip that constraint.
	 */
	Page<Product> searchProducts(Long categoryId, String name, Pageable pageable);

	/**
	 * Create a new product including image storage and wood price rows.
	 */
	Product save(ProductDto dto);

	/**
	 * Update an existing product by ID, refreshing images and wood prices.
	 */
	Product update(Long id, ProductDto dto);

	/**
	 * Delete a product and remove all associated image files from disk.
	 */
	void delete(Long id);

	/**
	 * All active wood price rows for a product. Rendered as the radio button
	 * selector on the product detail page.
	 */
	List<ProductWoodPrice> getWoodPrices(Long productId);

	/**
	 * Wood prices keyed by woodType ID — injected into the page as
	 * window.WOOD_PRICE_MAP for the JavaScript dynamic price calculator.
	 */
	Map<Long, ProductWoodPrice> getWoodPriceMap(Long productId);

	/**
	 * Calculate the final price for a product + wood type combination.
	 *
	 * Logic: if ProductWoodPrice.finalPriceOverride != null → use override else →
	 * product.basePrice + ProductWoodPrice.priceAdjustment
	 *
	 * Falls back to product.basePrice if no wood type is selected.
	 */
	BigDecimal calculatePrice(Long productId, Long woodTypeId);

	/** Count of active products — used by the admin dashboard stat card. */
	long countActive();
}
