package com.my.app.controller;

import com.epam.app.model.request.UpdateOrderStatusRequest;
import com.epam.app.model.response.OrderStatusResponse;
import com.my.app.facade.OrderFacade;
import com.my.app.model.converter.FromDtoToResponseOrderConverter;
import com.my.app.model.converter.FromDtoToResponseOrderStatusConverter;
import com.my.app.model.converter.FromRequestToDtoOrderConverter;
import com.my.app.model.converter.FromUpdateRequestToDtoOrderStatusConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderStatusDto;
import com.my.app.model.request.OrderRequest;
import com.my.app.model.response.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

@Slf4j
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {
    private OrderFacade orderFacade;
    private FromDtoToResponseOrderConverter toResponseOrderConverter;
    private FromRequestToDtoOrderConverter toDtoOrderConverter;
    private FromUpdateRequestToDtoOrderStatusConverter toDtoOrderStatusConverter;
    private FromDtoToResponseOrderStatusConverter toResponseOrderStatusConverter;

    @PreAuthorize("hasRole('customer') or hasRole('owner')")
    @GetMapping("/{id}/plain")
    public ResponseEntity<OrderResponse> getOrderByIdPlain(@PathVariable @NotNull @Min(1) Long id) {
        final OrderDto result = orderFacade.getOrderById(id, false);

        final OrderResponse convertedResult = toResponseOrderConverter.convert(result);
        log.info("Order response for (id = {}). Order: {}", id, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer') or hasRole('owner')")
    @GetMapping("/user/{id}/plain")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByUserIdPlain(@PathVariable @NotNull @Min(1) Long id) {
        final Collection<OrderDto> result = orderFacade.getAllOrdersByUserId(id, false);

        final List<OrderResponse> convertedResult = result.stream()
                .map(order -> toResponseOrderConverter.convert(order))
                .collect(toList());
        log.info("Orders response for (userId = {}). Orders: {}", id, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer') or hasRole('owner')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        final OrderDto result = orderFacade.getOrderById(id, true);

        final OrderResponse convertedResult = toResponseOrderConverter.convert(result);
        log.info("Order response for (id = {}). Order: {}", id, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer') or hasRole('owner')")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByUserId(@PathVariable @NotNull @Min(1) Long id) {
        final Collection<OrderDto> result = orderFacade.getAllOrdersByUserId(id, true);

        final List<OrderResponse> convertedResult = result.stream()
                .map(order -> toResponseOrderConverter.convert(order))
                .collect(toList());
        log.info("Orders response for (userId = {}). Orders: {}", id, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer') and #orderRequest.userId == #jwt.getClaim('customer_id')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest, @AuthenticationPrincipal Jwt jwt) {
        final OrderDto newOrderFields = toDtoOrderConverter.convert(orderRequest);
        final OrderDto createdOrder = orderFacade.createOrder(newOrderFields);

        final OrderResponse convertedResult = toResponseOrderConverter.convert(createdOrder);
        log.info("Created Order response for (request = {}) . New Order: {}", orderRequest, convertedResult);

        return new ResponseEntity<>(convertedResult, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('customer')")
    @PatchMapping("/status")
    public ResponseEntity<OrderStatusResponse> updateOrderStatus(@RequestBody @Valid UpdateOrderStatusRequest updateOrderStatusRequest) {
        final OrderStatusDto orderStatusUpdateFields = toDtoOrderStatusConverter.convert(updateOrderStatusRequest);
        final OrderStatusDto updatedOrderStatus = orderFacade.updateOrderStatus(orderStatusUpdateFields);

        final OrderStatusResponse convertedResult = toResponseOrderStatusConverter.convert(updatedOrderStatus);
        log.info("Updated Order Status response for (request = {}) . Updated Order Status: {}", updateOrderStatusRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }
}
