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

import com.brofurs.brofurs.dto.ProductDto;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.enums.StockStatus;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.services.CategoryService;
import com.brofurs.brofurs.services.ProductService;
import com.brofurs.brofurs.services.WoodTypeService;

import jakarta.validation.Valid;

/**
 * AdminProductController
 *
 * Full CRUD for furniture products including image upload and wood pricing.
 * Requires ADMIN role.
 *
 * GET  /admin/products            → list all products
 * GET  /admin/products/new        → blank create form
 * GET  /admin/products/edit/{id}  → pre-filled edit form with existing images + wood prices
 * POST /admin/products/save       → create or update product with images and wood prices
 * POST /admin/products/delete/{id}→ soft delete (sets active = false)
 */
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final WoodTypeService woodTypeService;

    public AdminProductController(ProductService productService,
                                  CategoryService categoryService,
                                  WoodTypeService woodTypeService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.woodTypeService = woodTypeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAllActive());
        return "admin/products/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("woodTypes", woodTypeService.findAllActive());
        model.addAttribute("stockStatuses", StockStatus.values());
        model.addAttribute("isNew", true);
        return "admin/products/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        // Map entity to DTO for form binding
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategoryId(product.getCategory().getId());
        dto.setBasePrice(product.getBasePrice());
        dto.setCustomSizeAllowed(product.isCustomSizeAllowed());
        dto.setStockStatus(product.getStockStatus());
        dto.setActive(product.isActive());

        model.addAttribute("product", dto);
        model.addAttribute("existingImages", product.getImages());
        model.addAttribute("existingWoodPrices", productService.getWoodPriceMap(id));
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("woodTypes", woodTypeService.findAllActive());
        model.addAttribute("stockStatuses", StockStatus.values());
        model.addAttribute("isNew", false);
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("product") ProductDto dto,
                       BindingResult result,
                       RedirectAttributes attrs,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllActive());
            model.addAttribute("woodTypes", woodTypeService.findAllActive());
            model.addAttribute("stockStatuses", StockStatus.values());
            model.addAttribute("isNew", dto.getId() == null);
            return "admin/products/form";
        }

        try {
            if (dto.getId() == null) {
                productService.save(dto);
                attrs.addFlashAttribute("successMessage", "Product created successfully.");
            } else {
                productService.update(dto.getId(), dto);
                attrs.addFlashAttribute("successMessage", "Product updated successfully.");
            }
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Error saving product: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            productService.delete(id);
            attrs.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Cannot delete: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }
}
