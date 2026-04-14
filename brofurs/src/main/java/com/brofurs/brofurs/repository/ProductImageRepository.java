package com.brofurs.brofurs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.ProductImage;

/**
 * Repository for ProductImage entity.
 *
 * Each product can have multiple images.
 * One image is flagged as the primary image and shown on product cards.
 * The rest appear in the gallery on the product detail page.
 */
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * All images belonging to a specific product.
     * Used when loading the gallery on the product detail page.
     */
    List<ProductImage> findByProductId(Long productId);

    /**
     * The primary (main) image for a product.
     * Used for product card thumbnails on listing and home pages.
     */
    Optional<ProductImage> findByProductIdAndPrimaryImageTrue(Long productId);
}