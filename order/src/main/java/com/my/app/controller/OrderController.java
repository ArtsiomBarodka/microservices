package com.my.app.controller;

import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToResponseOrderConverter;
import com.my.app.model.converter.FromRequestToDtoOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.request.OrderRequest;
import com.my.app.model.response.OrderResponse;
import com.my.app.service.OrderFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private OrderFacade orderFacade;
    private PropertiesConfig propertiesConfig;
    private FromDtoToResponseOrderConverter orderConverter;
    private FromRequestToDtoOrderConverter orderToDtoConverter;

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello from " + propertiesConfig.getName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        final OrderDto result = orderFacade.getOrderById(id);
        return ResponseEntity.ok(orderConverter.convert(result));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByUserId(@PathVariable @NotNull @Min(1) Long id) {
        final Collection<OrderDto> result = orderFacade.getAllOrdersByUserId(id);
        return ResponseEntity.ok(result.stream()
                .map(order -> orderConverter.convert(order))
                .collect(toList()));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        final OrderDto newOrderFields = orderToDtoConverter.convert(orderRequest);
        final OrderDto createdOrder = orderFacade.createOrder(newOrderFields);
        return new ResponseEntity<>(orderConverter.convert(createdOrder), HttpStatus.CREATED);
    }
}
