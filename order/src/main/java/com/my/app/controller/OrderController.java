package com.my.app.controller;

import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToVewUserConverter;
import com.my.app.model.converter.FromDtoToViewOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.view.OrderView;
import com.my.app.model.view.UserView;
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

import static java.util.stream.Collectors.toList;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private PropertiesConfig propertiesConfig;
    private FromDtoToViewOrderConverter orderConverter;
    private FromDtoToVewUserConverter userConverter;

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
    public ResponseEntity<Collection<UserView>> getAllUsers() {
        final Collection<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList.stream().map(userConverter::convert).collect(toList()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserView> getUserById(@PathVariable @NotNull @Min(1) Long id) {
        final UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userConverter.convert(userDto));
    }
}
