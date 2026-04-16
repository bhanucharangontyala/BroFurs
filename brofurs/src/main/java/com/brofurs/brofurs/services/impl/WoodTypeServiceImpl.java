package com.brofurs.brofurs.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brofurs.brofurs.dto.WoodTypeDto;
import com.brofurs.brofurs.entity.WoodType;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.repository.WoodTypeRepository;
import com.brofurs.brofurs.services.WoodTypeService;

/**
 * WoodTypeServiceImpl
 *
 * Implements CRUD operations for wood types (Neem, Mango, Teak, Other). Wood
 * types are referenced by ProductWoodPrice for pricing and by the product
 * detail page wood selector.
 */
@Service
@Transactional
public class WoodTypeServiceImpl implements WoodTypeService {

	private final WoodTypeRepository woodTypeRepository;

	public WoodTypeServiceImpl(WoodTypeRepository woodTypeRepository) {
		this.woodTypeRepository = woodTypeRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<WoodType> findAllActive() {
		return woodTypeRepository.findByActiveTrueOrderByNameAsc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<WoodType> findAll() {
		return woodTypeRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<WoodType> findById(Long id) {
		return woodTypeRepository.findById(id);
	}

	@Override
	public WoodType save(WoodTypeDto dto) {
		WoodType woodType = new WoodType();
		mapDtoToEntity(dto, woodType);
		return woodTypeRepository.save(woodType);
	}

	@Override
	public WoodType update(Long id, WoodTypeDto dto) {
		WoodType woodType = woodTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("WoodType", id));
		mapDtoToEntity(dto, woodType);
		return woodTypeRepository.save(woodType);
	}

	@Override
	public void delete(Long id) {
		WoodType woodType = woodTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("WoodType", id));
		woodTypeRepository.delete(woodType);
	}

	private void mapDtoToEntity(WoodTypeDto dto, WoodType woodType) {
		woodType.setName(dto.getName());
		woodType.setDescription(dto.getDescription());
		woodType.setActive(dto.isActive());
	}
}
