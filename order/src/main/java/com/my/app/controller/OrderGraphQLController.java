package com.my.app.controller;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UserDto;
import com.my.app.service.OrderService;
import com.my.app.service.ProductService;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
@Controller
public class OrderGraphQLController {
    private OrderService orderService;
    private UserService userService;
    private ProductService productService;

    @PreAuthorize("hasRole('customer') or hasRole('owner')")
    @QueryMapping(name = "order")
    public OrderDto getOrderById(@Argument Long id) {
        return orderService.getOrderById(id);
    }

    @PreAuthorize("hasRole('customer') or hasRole('owner')")
    @QueryMapping(name = "orders")
    public Collection<OrderDto> getOrdersByUserId(@Argument Long userId) {
        return orderService.getAllOrdersByUserId(userId);
    }

    @SchemaMapping(typeName = "Order", field = "user")
    public UserDto user(OrderDto order) {
        return userService.getUserById(order.getUser().getId());
    }

    @BatchMapping(typeName = "OrderItem", field = "product")
    public Map<OrderItemDto, ProductDto> getProductsInBatch(List<OrderItemDto> orderItems) {
        final Set<String> ids = orderItems.stream()
                .map(OrderItemDto::getProduct)
                .map(ProductDto::getId)
                .collect(Collectors.toSet());

        final List<ProductDto> products = productService.getAllProductsByIds(ids);

        final Map<String, ProductDto> productsMap = products.stream()
                .collect(toMap(ProductDto::getId, Function.identity()));

        return orderItems.stream()
                .collect(toMap(orderItem -> orderItem, orderItem -> productsMap.get(orderItem.getProduct().getId())));
    }
}
