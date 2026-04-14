package com.brofurs.brofurs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.WoodType;

/**
 * Repository for WoodType entity.
 *
 * Wood types (Neem, Mango, Teak, Other) are the core pricing variable in FurniCraft.
 * Active types appear in the product detail page wood selector and admin product form.
 */
public interface WoodTypeRepository extends JpaRepository<WoodType, Long> {

    /**
     * All active wood types sorted by name.
     * Used to populate:
     *  - Wood type selector on the product detail page
     *  - Wood pricing table on the admin product form
     *  - Dropdown on the admin wood types list
     */
    List<WoodType> findByActiveTrueOrderByNameAsc();
}
