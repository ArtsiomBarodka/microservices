package com.my.app.controller;

import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToViewOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.view.OrderView;
import com.my.app.service.OrderService;
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
}
