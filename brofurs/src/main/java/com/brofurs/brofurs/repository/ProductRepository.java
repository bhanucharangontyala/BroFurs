package com.brofurs.brofurs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brofurs.brofurs.entity.Product;

/**
 * Repository for Product entity.
 *
 * Key queries: - findBySlug: resolves a URL slug to a product for the detail
 * page - findByActiveTrueOrderByCreatedAtDesc: lists all active products
 * newest-first - searchProducts: paginated search combining optional category
 * and name filters - countByActiveTrue: used for the admin dashboard "Total
 * Products" stat card
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Find a product by its URL-friendly slug. Used by ProductController to resolve
	 * /products/{slug}.
	 */
	@Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.slug = :slug")
	Optional<Product> findBySlugWithImages(@Param("slug") String slug);
	
	@Query("SELECT p FROM Product p LEFT JOIN FETCH p.images")
	List<Product> findAllWithImages();

	/**
	 * All active products sorted newest-first. Used for the admin products list and
	 * the featured products section on home.
	 */
	List<Product> findByActiveTrueOrderByCreatedAtDesc();

	/**
	 * Active products in a specific category, newest-first. Used when filtering
	 * products by category.
	 */
	List<Product> findByCategoryIdAndActiveTrueOrderByCreatedAtDesc(Long categoryId);

	/**
	 * Paginated search across active products. Both categoryId and name are
	 * optional — passing null skips that filter.
	 *
	 * @param categoryId filter by category (null = all categories)
	 * @param name       substring search on product name (null = no name filter)
	 * @param pageable   pagination and sort settings
	 */
	@Query("""
		    SELECT DISTINCT p 
		    FROM Product p 
		    LEFT JOIN FETCH p.images
		    WHERE p.active = true
		      AND (:categoryId IS NULL OR p.category.id = :categoryId)
		      AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
		""")
		Page<Product> searchProducts(@Param("categoryId") Long categoryId,
		                             @Param("name") String name,
		                             Pageable pageable);

	/**
	 * Count of active products — shown on the admin dashboard stat card.
	 */
	long countByActiveTrue();
}