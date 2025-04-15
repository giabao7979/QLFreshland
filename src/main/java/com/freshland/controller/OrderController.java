package com.freshland.controller;

import com.freshland.model.Order;
import com.freshland.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "views/admin/order/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            model.addAttribute("statuses", Order.OrderStatus.values());
            return "views/admin/order/view";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found!");
            return "redirect:/admin/orders";
        }
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, 
                                   @RequestParam Order.OrderStatus status,
                                   RedirectAttributes redirectAttributes) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            if (updatedOrder != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Order not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }
        
        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/filter")
    public String filterOrders(@RequestParam(required = false) Order.OrderStatus status,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              Model model) {
        List<Order> filteredOrders;
        
        if (status != null) {
            filteredOrders = orderService.getOrdersByStatus(status);
            model.addAttribute("selectedStatus", status);
        } else if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            filteredOrders = orderService.getOrdersByDateRange(startDateTime, endDateTime);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } else {
            filteredOrders = orderService.getAllOrders();
        }
        
        model.addAttribute("orders", filteredOrders);
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "views/admin/order/list";
    }
}