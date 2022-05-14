package com.my.app.controller;

import com.my.app.config.PropertiesConfig;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.User;
import com.my.app.service.OrderService;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private PropertiesConfig propertiesConfig;

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello from " + propertiesConfig.getName();
    }

    @GetMapping
    public ResponseEntity<Collection<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @NotNull @Min(1) Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
