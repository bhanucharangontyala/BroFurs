package com.brofurs.brofurs.services;

import java.util.List;
import java.util.Optional;

import com.brofurs.brofurs.dto.CategoryDto;
import com.brofurs.brofurs.entity.Category;

/**
 * CategoryService
 *
 * Business logic for furniture categories.
 *
 * findAllActive() → used by public pages (navbar, product filter sidebar, home
 * page) findAll() → used by admin category list (includes inactive) save /
 * update → admin create / edit form delete → admin delete action
 */
public interface CategoryService {

	/**
	 * All active categories sorted by name — for public-facing dropdowns and grids.
	 */
	List<Category> findAllActive();

	/**
	 * All categories regardless of active status — for the admin management list.
	 */
	List<Category> findAll();

	/**
	 * Find a single category by ID — used before showing the edit form.
	 */
	Optional<Category> findById(Long id);

	/**
	 * Create a new category from the admin form DTO.
	 */
	Category save(CategoryDto dto);

	/**
	 * Update an existing category by ID from the admin form DTO.
	 */
	Category update(Long id, CategoryDto dto);

	/**
	 * Delete a category by ID. Throws ResourceNotFoundException if not found.
	 */
	void delete(Long id);
}