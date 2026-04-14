package com.brofurs.brofurs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByActiveTrueOrderByNameAsc();

	boolean existsByNameIgnoreCase(String name);
}