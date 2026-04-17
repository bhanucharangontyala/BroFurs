package com.brofurs.brofurs.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.services.CategoryService;
import com.brofurs.brofurs.services.ProductService;
import com.brofurs.brofurs.services.UserService;

/**
 * ProductController
 *
 * Public-facing product pages — no login required.
 *
 * GET /products          → paginated product listing with category + name filters
 * GET /products/{slug}   → single product detail page with wood type price map,
 *                          order form, and appointment form
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    // ── Product Listing ───────────────────────────────
    @GetMapping
    public String list(@RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String name,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       Model model) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productService.searchProducts(categoryId, name, pageable);

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("searchName", name);
        model.addAttribute("currentPage", page);

        return "products/list";
    }

    // ── Product Detail ────────────────────────────────
    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {

        Product product = productService.findBySlugWithImages(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + slug));
        model.addAttribute("product", product);
        model.addAttribute("products", productService.findAllActive());
        // Wood price list (for the radio selector) and map (for JavaScript price update)
        model.addAttribute("woodPrices", productService.getWoodPrices(product.getId()));
        model.addAttribute("woodPriceMap", productService.getWoodPriceMap(product.getId()));

        // Pass current user to pre-fill order and appointment forms
        if (userDetails != null) {
            User currentUser = userService.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        return "products/details";
    }
}
