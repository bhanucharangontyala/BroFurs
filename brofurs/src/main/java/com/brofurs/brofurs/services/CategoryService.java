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
 * page) 
 * findAll() → used by admin category list (includes inactive) save /
 * update → admin create / edit form delete → admin delete action
 */
public interface CategoryService {

	List<Category> findAllActive();

	List<Category> findAll();

	Optional<Category> findById(Long id);

	Category save(CategoryDto dto);

	Category update(Long id, CategoryDto dto);

	void delete(Long id);
}