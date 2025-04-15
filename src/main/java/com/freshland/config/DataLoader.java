package com.freshland.config;

import com.freshland.model.Category;
import com.freshland.model.Product;
import com.freshland.model.User;
import com.freshland.repository.CategoryRepository;
import com.freshland.repository.ProductRepository;
import com.freshland.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Check if data already exists
        if (categoryRepository.count() > 0) {
            return;
        }
        
        // Create sample categories
        List<Category> categories = createCategories();
        
        // Create sample products
        createProducts(categories);
        
        // Create sample users
        createUsers();
    }
    
    private List<Category> createCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");
        fruits.setDescription("Fresh fruits from local farmers");
        
        Category vegetables = new Category();
        vegetables.setName("Vegetables");
        vegetables.setDescription("Organic vegetables for your healthy diet");
        
        Category dairy = new Category();
        dairy.setName("Dairy");
        dairy.setDescription("Milk, cheese, yogurt and other dairy products");
        
        Category bakery = new Category();
        bakery.setName("Bakery");
        bakery.setDescription("Fresh bread, pastries and cakes");
        
        Category beverages = new Category();
        beverages.setName("Beverages");
        beverages.setDescription("Soft drinks, juices, coffee and tea");
        
        return categoryRepository.saveAll(Arrays.asList(fruits, vegetables, dairy, bakery, beverages));
    }
    
    private void createProducts(List<Category> categories) {
        Category fruits = categories.get(0);
        Category vegetables = categories.get(1);
        Category dairy = categories.get(2);
        Category bakery = categories.get(3);
        Category beverages = categories.get(4);
        
        // Fruits
        Product apple = new Product();
        apple.setName("Organic Apples");
        apple.setDescription("Sweet and crunchy organic apples. Perfect for snacking or baking.");
        apple.setPrice(new BigDecimal("3.99"));
        apple.setQuantity(50);
        apple.setCategory(fruits);
        apple.setImageUrl("/images/products/apple.jpg");
        
        Product banana = new Product();
        banana.setName("Organic Bananas");
        banana.setDescription("Sweet and nutritious organic bananas.");
        banana.setPrice(new BigDecimal("2.49"));
        banana.setQuantity(45);
        banana.setCategory(fruits);
        banana.setImageUrl("/images/products/banana.jpg");
        
        // Vegetables
        Product carrot = new Product();
        carrot.setName("Organic Carrots");
        carrot.setDescription("Fresh organic carrots. Rich in vitamins and antioxidants.");
        carrot.setPrice(new BigDecimal("1.99"));
        carrot.setQuantity(60);
        carrot.setCategory(vegetables);
        carrot.setImageUrl("/images/products/carrot.jpg");
        
        Product tomato = new Product();
        tomato.setName("Organic Tomatoes");
        tomato.setDescription("Juicy organic tomatoes. Great for salads and cooking.");
        tomato.setPrice(new BigDecimal("2.99"));
        tomato.setQuantity(40);
        tomato.setCategory(vegetables);
        tomato.setImageUrl("/images/products/tomato.jpg");
        
        // Dairy
        Product milk = new Product();
        milk.setName("Organic Milk");
        milk.setDescription("Fresh organic whole milk. Source of calcium and protein.");
        milk.setPrice(new BigDecimal("4.49"));
        milk.setQuantity(30);
        milk.setCategory(dairy);
        milk.setImageUrl("/images/products/milk.jpg");
        
        Product cheese = new Product();
        cheese.setName("Organic Cheddar Cheese");
        cheese.setDescription("Delicious organic cheddar cheese. Perfect for sandwiches and snacks.");
        cheese.setPrice(new BigDecimal("5.99"));
        cheese.setQuantity(25);
        cheese.setCategory(dairy);
        cheese.setImageUrl("/images/products/cheese.jpg");
        
        // Bakery
        Product bread = new Product();
        bread.setName("Whole Grain Bread");
        bread.setDescription("Freshly baked whole grain bread. Nutritious and delicious.");
        bread.setPrice(new BigDecimal("3.49"));
        bread.setQuantity(20);
        bread.setCategory(bakery);
        bread.setImageUrl("/images/products/bread.jpg");
        
        Product croissant = new Product();
        croissant.setName("Butter Croissants");
        croissant.setDescription("Flaky and buttery croissants. Perfect for breakfast.");
        croissant.setPrice(new BigDecimal("1.99"));
        croissant.setQuantity(15);
        croissant.setCategory(bakery);
        croissant.setImageUrl("/images/products/croissant.jpg");
        
        // Beverages
        Product juice = new Product();
        juice.setName("Orange Juice");
        juice.setDescription("Freshly squeezed orange juice. Rich in vitamin C.");
        juice.setPrice(new BigDecimal("3.99"));
        juice.setQuantity(35);
        juice.setCategory(beverages);
        juice.setImageUrl("/images/products/juice.jpg");
        
        Product coffee = new Product();
        coffee.setName("Organic Coffee");
        coffee.setDescription("Premium organic coffee beans. Rich and aromatic.");
        coffee.setPrice(new BigDecimal("8.99"));
        coffee.setQuantity(30);
        coffee.setCategory(beverages);
        coffee.setImageUrl("/images/products/coffee.jpg");
        
        productRepository.saveAll(Arrays.asList(
            apple, banana, carrot, tomato, milk, cheese, bread, croissant, juice, coffee
        ));
    }
    
    private void createUsers() {
        // Admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123"); // In a real app, this would be encrypted
        admin.setFullName("Admin User");
        admin.setEmail("admin@freshland.com");
        admin.setRole(User.Role.ADMIN);
        
        // Staff user
        User staff = new User();
        staff.setUsername("staff");
        staff.setPassword("staff123"); // In a real app, this would be encrypted
        staff.setFullName("Staff User");
        staff.setEmail("staff@freshland.com");
        staff.setRole(User.Role.STAFF);
        
        // Customer user
        User customer = new User();
        customer.setUsername("customer");
        customer.setPassword("customer123"); // In a real app, this would be encrypted
        customer.setFullName("Customer User");
        customer.setEmail("customer@example.com");
        customer.setRole(User.Role.CUSTOMER);
        
        userRepository.saveAll(Arrays.asList(admin, staff, customer));
    }
}