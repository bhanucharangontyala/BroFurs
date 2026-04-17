package com.brofurs.brofurs.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brofurs.brofurs.dto.OrderRequestDto;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.services.OrderService;
import com.brofurs.brofurs.services.ProductService;
import com.brofurs.brofurs.services.UserService;

import jakarta.validation.Valid;

/**
 * OrderController
 *
 * Handles order placement from the product detail page.
 * Requires USER role (enforced by SecurityConfig).
 *
 * POST /order/place → validates the order form, creates the Order and OrderItem,
 *                     then redirects to the user's orders page with a success message.
 *
 * On validation failure, redirects back to the product detail page
 * with an error flash message.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    public OrderController(OrderService orderService,
                           UserService userService,
                           ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/place")
    public String placeOrder(@Valid @ModelAttribute OrderRequestDto dto,
                             BindingResult result,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes attrs) {

        // On validation error redirect back to the product page
        if (result.hasErrors()) {
            attrs.addFlashAttribute("errorMessage", "Please fill in all required fields correctly.");
            String slug = resolveProductSlug(dto.getProductId());
            return "redirect:/products/" + slug;
        }

        // Resolve the logged-in user
        User user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));

        // Place the order — returns the saved Order with generated order number
        orderService.placeOrder(dto, user);

        attrs.addFlashAttribute("successMessage",
            "Your order has been placed successfully! We will contact you to confirm.");
        return "redirect:/user/orders";
    }

    // Resolve product slug for redirect on error (fallback to listing if not found)
    private String resolveProductSlug(Long productId) {
        if (productId == null) return "";
        return productService.findById(productId)
            .map(Product::getSlug)
            .orElse("");
    }
}
