package com.freshland.service;

import com.freshland.model.Category;
import com.freshland.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> getProductsByCategory(Category category);
    List<Product> searchProducts(String keyword);
    List<Product> getLowStockProducts();
    Product saveProduct(Product product);
    void deleteProduct(Long id);
}