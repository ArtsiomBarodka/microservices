package com.my.app.controller;

import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToResponseOrderConverter;
import com.my.app.model.converter.FromRequestToDtoOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.request.OrderRequest;
import com.my.app.model.response.OrderResponse;
import com.my.app.service.OrderFacade;
import com.my.app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private OrderFacade orderFacade;
    private PropertiesConfig propertiesConfig;
    private FromDtoToResponseOrderConverter orderConverter;
    private FromRequestToDtoOrderConverter orderToDtoConverter;

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello from " + propertiesConfig.getName();
    }

    @GetMapping
    public ResponseEntity<Collection<OrderResponse>> getAllOrders() {
        final Collection<OrderDto> orderDtoList = orderFacade.getAllOrders();
        return ResponseEntity.ok(orderDtoList.stream().map(orderConverter::convert).collect(toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        final OrderDto orderDto = orderFacade.getOrderById(id);
        return ResponseEntity.ok(orderConverter.convert(orderDto));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        final OrderDto orderDto = orderToDtoConverter.convert(orderRequest);
        final OrderDto order = orderFacade.createOrder(orderDto);
        final OrderResponse convertedOrder = orderConverter.convert(order);
        return new ResponseEntity<>(convertedOrder, HttpStatus.CREATED);
    }
}
