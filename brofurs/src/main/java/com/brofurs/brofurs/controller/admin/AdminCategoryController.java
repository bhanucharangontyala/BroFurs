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

import com.brofurs.brofurs.dto.CategoryDto;
import com.brofurs.brofurs.entity.Category;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.services.CategoryService;

import jakarta.validation.Valid;

/**
 * AdminCategoryController
 *
 * Full CRUD for furniture categories. Requires ADMIN role.
 *
 * GET  /admin/categories            → list all categories
 * GET  /admin/categories/new        → blank create form
 * GET  /admin/categories/edit/{id}  → pre-filled edit form
 * POST /admin/categories/save       → create or update
 * POST /admin/categories/delete/{id}→ delete
 */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        model.addAttribute("isNew", true);
        return "admin/categories/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setActive(category.isActive());

        model.addAttribute("category", dto);
        model.addAttribute("isNew", false);
        return "admin/categories/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("category") CategoryDto dto,
                       BindingResult result,
                       RedirectAttributes attrs,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("isNew", dto.getId() == null);
            return "admin/categories/form";
        }

        try {
            if (dto.getId() == null) {
                categoryService.save(dto);
                attrs.addFlashAttribute("successMessage", "Category created successfully.");
            } else {
                categoryService.update(dto.getId(), dto);
                attrs.addFlashAttribute("successMessage", "Category updated successfully.");
            }
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }

        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            categoryService.delete(id);
            attrs.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Cannot delete: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}