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
 * product form wood pricing table 
 * findAll() → used by the admin wood types
 * management list
 */
public interface WoodTypeService {

	List<WoodType> findAllActive();

	List<WoodType> findAll();

	Optional<WoodType> findById(Long id);

	WoodType save(WoodTypeDto dto);

	WoodType update(Long id, WoodTypeDto dto);

	void delete(Long id);
}