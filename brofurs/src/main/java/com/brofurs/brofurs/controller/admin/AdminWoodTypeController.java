package com.brofurs.brofurs.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brofurs.brofurs.dto.WoodTypeDto;
import com.brofurs.brofurs.entity.WoodType;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.services.WoodTypeService;

import jakarta.validation.Valid;

/**
 * AdminWoodTypeController
 *
 * Full CRUD for wood types (Neem, Mango, Teak, Other, etc.).
 * Requires ADMIN role.
 *
 * GET  /admin/wood-types             → list all wood types
 * GET  /admin/wood-types/new         → blank create form
 * GET  /admin/wood-types/edit/{id}   → pre-filled edit form
 * POST /admin/wood-types/save        → create or update
 * POST /admin/wood-types/delete/{id} → delete
 */
@Controller
@RequestMapping("/admin/wood-types")
public class AdminWoodTypeController {

    private final WoodTypeService woodTypeService;

    public AdminWoodTypeController(WoodTypeService woodTypeService) {
        this.woodTypeService = woodTypeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("woodTypes", woodTypeService.findAll());
        return "admin/wood-types/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("woodType", new WoodTypeDto());
        model.addAttribute("isNew", true);
        return "admin/wood-types/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        WoodType wt = woodTypeService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wood type not found: " + id));

        WoodTypeDto dto = new WoodTypeDto();
        dto.setId(wt.getId());
        dto.setName(wt.getName());
        dto.setDescription(wt.getDescription());
        dto.setActive(wt.isActive());

        model.addAttribute("woodType", dto);
        model.addAttribute("isNew", false);
        return "admin/wood-types/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("woodType") WoodTypeDto dto,
                       BindingResult result,
                       RedirectAttributes attrs,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("isNew", dto.getId() == null);
            return "admin/wood-types/form";
        }

        try {
            if (dto.getId() == null) {
                woodTypeService.save(dto);
                attrs.addFlashAttribute("successMessage", "Wood type created successfully.");
            } else {
                woodTypeService.update(dto.getId(), dto);
                attrs.addFlashAttribute("successMessage", "Wood type updated successfully.");
            }
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }

        return "redirect:/admin/wood-types";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            woodTypeService.delete(id);
            attrs.addFlashAttribute("successMessage", "Wood type deleted successfully.");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Cannot delete: " + e.getMessage());
        }
        return "redirect:/admin/wood-types";
    }
}
