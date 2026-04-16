package com.brofurs.brofurs.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brofurs.brofurs.dto.CategoryDto;
import com.brofurs.brofurs.entity.Category;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.repository.CategoryRepository;
import com.brofurs.brofurs.services.CategoryService;

/**
 * CategoryServiceImpl
 *
 * Implements CRUD operations for furniture categories. Delegates persistence to
 * CategoryRepository.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> findAllActive() {
		return categoryRepository.findByActiveTrueOrderByNameAsc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Category> findById(Long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public Category save(CategoryDto dto) {
		Category category = new Category();
		mapDtoToEntity(dto, category);
		return categoryRepository.save(category);
	}

	@Override
	public Category update(Long id, CategoryDto dto) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", id));
		mapDtoToEntity(dto, category);
		return categoryRepository.save(category);
	}

	@Override
	public void delete(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", id));
		categoryRepository.delete(category);
	}

	// Copy DTO fields onto the entity
	private void mapDtoToEntity(CategoryDto dto, Category category) {
		category.setName(dto.getName());
		category.setDescription(dto.getDescription());
		category.setActive(dto.isActive());
	}
}
