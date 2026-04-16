package com.brofurs.brofurs.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * WoodTypeDto
 *
 * Used for creating and updating wood types (Neem, Mango, Teak, Other)
 * via the admin wood types form.
 *
 * Note: price adjustments per wood type are stored on ProductWoodPrice,
 * not on WoodType itself — so no price fields are needed here.
 */
@Getter
@Setter
public class WoodTypeDto {

    // Null for new wood type, populated for edit
    private Long id;

    @NotBlank(message = "Wood type name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    // Controls visibility in the product detail wood selector
    private boolean active = true;
}