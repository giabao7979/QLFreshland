package com.freshland.service;

import com.freshland.model.Order;
import com.freshland.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    List<Order> getOrdersByStatus(Order.OrderStatus status);
    List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end);
    Order saveOrder(Order order);
    Order updateOrderStatus(Long id, Order.OrderStatus status);
    void deleteOrder(Long id);
}