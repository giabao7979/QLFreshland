package com.freshland.controller;

import com.freshland.model.Category;
import com.freshland.model.Product;
import com.freshland.service.CategoryService;
import com.freshland.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ShopController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String shopHome(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "views/shop/shop";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            // Add related products (same category)
            Product product = productOptional.get();
            if (product.getCategory() != null) {
                List<Product> relatedProducts = productService.getProductsByCategory(product.getCategory());
                relatedProducts.removeIf(p -> p.getId().equals(product.getId()));
                model.addAttribute("relatedProducts", relatedProducts);
            }
            return "views/shop/product-detail";
        } else {
            return "redirect:/shop";
        }
    }

    @GetMapping("/category/{id}")
    public String productsByCategory(@PathVariable Long id, Model model) {
        Optional<Category> categoryOptional = categoryService.getCategoryById(id);
        
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            model.addAttribute("category", category);
            model.addAttribute("products", productService.getProductsByCategory(category));
            model.addAttribute("categories", categoryService.getAllCategories());
            return "views/shop/category";
        } else {
            return "redirect:/shop";
        }
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("products", productService.searchProducts(keyword));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "views/shop/search-results";
    }
}