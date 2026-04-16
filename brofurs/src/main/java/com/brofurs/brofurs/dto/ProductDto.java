package com.brofurs.brofurs.dto;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.brofurs.brofurs.enums.StockStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ProductDto
 *
 * Used for both creating and updating a product via the admin product form.
 * Handles text fields, file uploads, and parallel lists for wood type pricing.
 *
 * Wood pricing is submitted as three parallel lists:
 *   woodTypeIds[]        → Long IDs of each wood type row shown in the form
 *   priceAdjustments[]  → price adjustment added to basePrice (can be 0)
 *   finalPriceOverrides[] → if set, overrides basePrice + adjustment entirely
 */
@Getter
@Setter
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;

    private String description;

    @NotNull(message = "Please select a category")
    private Long categoryId;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal basePrice;

    private boolean customSizeAllowed = false;

    private StockStatus stockStatus = StockStatus.IN_STOCK;

    private boolean active = true;

    // Primary (thumbnail) image file upload
    private MultipartFile primaryImageFile;

    // Additional gallery image file uploads
    private List<MultipartFile> additionalImageFiles;

    // Parallel lists for wood type price rows in the form table
    private List<Long> woodTypeIds;
    private List<BigDecimal> priceAdjustments;
    private List<BigDecimal> finalPriceOverrides;
}
