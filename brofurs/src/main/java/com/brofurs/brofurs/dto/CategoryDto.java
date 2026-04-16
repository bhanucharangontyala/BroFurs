package com.brofurs.brofurs.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * CategoryDto
 *
 * Used for creating and updating furniture categories via the admin form.
 * The id field is null when creating a new category and populated when editing.
 */
@Getter
@Setter
public class CategoryDto {

    // Null for new category, populated for edit
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    // Controls visibility on the public site
    private boolean active = true;
}
