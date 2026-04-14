package com.brofurs.brofurs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.ProductWoodPrice;

/**
 * Repository for ProductWoodPrice entity.
 *
 * This is the junction table between Product and WoodType that stores
 * the price adjustment (or override) for each combination.
 *
 * Price calculation logic:
 *   - If finalPriceOverride is NOT NULL  → use finalPriceOverride as the price
 *   - If finalPriceOverride IS NULL      → finalPrice = product.basePrice + priceAdjustment
 *
 * Example seed data:
 *   Neem  → adjustment: +0      (same as base price)
 *   Mango → adjustment: +500
 *   Teak  → adjustment: +3000
 *   Other → adjustment: +1000
 */
public interface ProductWoodPriceRepository extends JpaRepository<ProductWoodPrice, Long> {

    /**
     * All active wood price entries for a given product.
     * Used to render the wood type selector and price map on the product detail page.
     */
    List<ProductWoodPrice> findByProductIdAndActiveTrueOrderByWoodTypeNameAsc(Long productId);

    /**
     * Exact match for a product + wood type pair.
     * Used when saving/updating product wood prices — if a record already exists
     * for this combination it is updated rather than creating a duplicate.
     */
    Optional<ProductWoodPrice> findByProductIdAndWoodTypeId(Long productId, Long woodTypeId);
}