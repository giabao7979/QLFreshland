package com.freshland.dto;

import com.freshland.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    
    private Long userId;
    
    private String username;
    
    private LocalDateTime orderDate;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    private Order.OrderStatus status;
    
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    private List<OrderItemDTO> items = new ArrayList<>();
}