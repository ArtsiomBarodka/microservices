package com.my.app.controller;

import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToViewOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.User;
import com.my.app.model.view.OrderView;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private PropertiesConfig propertiesConfig;
    private FromDtoToViewOrderConverter orderConverter;

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello from " + propertiesConfig.getName();
    }

    @GetMapping
    public ResponseEntity<Collection<OrderView>> getAllOrders() {
        final Collection<OrderDto> orderDtoList = orderService.getAllOrders();
        return ResponseEntity.ok(orderDtoList.stream().map(orderConverter::convert).collect(toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderView> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        final OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderConverter.convert(orderDto));
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
