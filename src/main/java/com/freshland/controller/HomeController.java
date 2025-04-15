package com.freshland.controller;

import com.freshland.service.CategoryService;
import com.freshland.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "views/home";
    }

    @GetMapping("/login")
    public String login() {
        return "views/login";
    }

    @GetMapping("/register")
    public String register() {
        return "views/register";
    }

    @GetMapping("/about")
    public String about() {
        return "views/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "views/contact";
    }
}