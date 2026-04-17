package com.brofurs.brofurs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.brofurs.brofurs.services.CategoryService;
import com.brofurs.brofurs.services.ProductService;

/**
 * HomeController
 *
 * Handles the public home page at "/" and "/home".
 * Loads featured products (latest 8) and all active categories
 * for the hero section, category grid, and featured product cards.
 */
@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService,
                          CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.findFeatured());
        model.addAttribute("categories", categoryService.findAllActive());
        return "home";
    }
}