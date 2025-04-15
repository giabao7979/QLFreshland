package com.freshland.controller;

import com.freshland.dto.ProductDTO;
import com.freshland.model.Category;
import com.freshland.model.Product;
import com.freshland.service.CategoryService;
import com.freshland.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "views/admin/product/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "views/admin/product/form";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                               BindingResult result,
                               @RequestParam("productImage") MultipartFile file,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "views/admin/product/form";
        }
        
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        
        // Set category
        if (productDTO.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryService.getCategoryById(productDTO.getCategoryId());
            categoryOptional.ifPresent(product::setCategory);
        }
        
        // Handle file upload
        if (!file.isEmpty()) {
            try {
                // Define the upload directory
                String uploadDir = "src/main/resources/static/images/products/";
                Path uploadPath = Paths.get(uploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // Generate unique filename
                String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath);
                
                // Set image URL for the product
                product.setImageUrl("/images/products/" + filename);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error uploading file: " + e.getMessage());
                return "redirect:/admin/products";
            }
        }
        
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
        
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.getProductById(id);
        
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            
            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());
                productDTO.setCategoryName(product.getCategory().getName());
            }
            
            productDTO.setImageUrl(product.getImageUrl());
            
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "views/admin/product/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                               @Valid @ModelAttribute("product") ProductDTO productDTO,
                               BindingResult result,
                               @RequestParam("productImage") MultipartFile file,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "views/admin/product/form";
        }
        
        Optional<Product> productOptional = productService.getProductById(id);
        
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            
            // Set category
            if (productDTO.getCategoryId() != null) {
                Optional<Category> categoryOptional = categoryService.getCategoryById(productDTO.getCategoryId());
                categoryOptional.ifPresent(product::setCategory);
            } else {
                product.setCategory(null);
            }
            
            // Handle file upload
            if (!file.isEmpty()) {
                try {
                    // Define the upload directory
                    String uploadDir = "src/main/resources/static/images/products/";
                    Path uploadPath = Paths.get(uploadDir);
                    
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    
                    // Generate unique filename
                    String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(filename);
                    Files.copy(file.getInputStream(), filePath);
                    
                    // Set image URL for the product
                    product.setImageUrl("/images/products/" + filename);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error uploading file: " + e.getMessage());
                    return "redirect:/admin/products";
                }
            }
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
        }
        
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
        }
        
        return "redirect:/admin/products";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        model.addAttribute("products", productService.searchProducts(keyword));
        model.addAttribute("keyword", keyword);
        return "views/admin/product/list";
    }
    
    @GetMapping("/low-stock")
    public String lowStockProducts(Model model) {
        model.addAttribute("products", productService.getLowStockProducts());
        model.addAttribute("lowStockFilter", true);
        return "views/admin/product/list";
    }
}