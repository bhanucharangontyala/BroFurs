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
 * Public-facing methods: 
 * findFeatured() → latest 8 active products for the home page 
 * searchProducts() → paginated filtered listing for the products page
 * findBySlug() → single product for the detail page 
 * getWoodPrices() → list of wood pricing rows for the radio selector 
 * getWoodPriceMap() → Map<woodTypeId,ProductWoodPrice> injected as JSON for JS 
 * calculatePrice() → final price for a product + wood type combination
 *
 * Admin methods: 
 * findAllActive() → full list for the admin product table 
 * save()/ 
 * update() → create / edit with image upload and wood pricing 
 * delete() → hard delete with image file cleanup 
 * countActive() → dashboard stat card
 */
public interface ProductService {

	List<Product> findFeatured();

	List<Product> findAllActive();

	Optional<Product> findById(Long id);

	Optional<Product> findBySlugWithImages(String slug);

	Page<Product> searchProducts(Long categoryId, String name, Pageable pageable);

	Product save(ProductDto dto);

	Product update(Long id, ProductDto dto);

	void delete(Long id);

	List<ProductWoodPrice> getWoodPrices(Long productId);

	Map<Long, ProductWoodPrice> getWoodPriceMap(Long productId);

	BigDecimal calculatePrice(Long productId, Long woodTypeId);

	long countActive();
}
