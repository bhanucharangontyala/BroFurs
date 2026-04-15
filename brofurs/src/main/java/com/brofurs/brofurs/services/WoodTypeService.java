package com.brofurs.brofurs.services;

import java.util.List;
import java.util.Optional;

import com.brofurs.brofurs.dto.WoodTypeDto;
import com.brofurs.brofurs.entity.WoodType;

/**
 * WoodTypeService
 *
 * Business logic for wood types (Neem, Mango, Teak, Other).
 *
 * findAllActive() → used by the product detail page wood selector and the admin
 * product form wood pricing table findAll() → used by the admin wood types
 * management list
 */
public interface WoodTypeService {

	/**
	 * All active wood types sorted by name — for public selectors and admin forms.
	 */
	List<WoodType> findAllActive();

	/**
	 * All wood types including inactive — for the admin management list.
	 */
	List<WoodType> findAll();

	/**
	 * Find a single wood type by ID — used before showing the edit form.
	 */
	Optional<WoodType> findById(Long id);

	/**
	 * Create a new wood type from the admin form DTO.
	 */
	WoodType save(WoodTypeDto dto);

	/**
	 * Update an existing wood type by ID from the admin form DTO.
	 */
	WoodType update(Long id, WoodTypeDto dto);

	/**
	 * Delete a wood type by ID. Throws ResourceNotFoundException if not found.
	 */
	void delete(Long id);
}