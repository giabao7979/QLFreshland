package com.freshland.repository;

import com.freshland.model.Category;
import com.freshland.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.quantity <= 10")
    List<Product> findLowStockProducts();
}