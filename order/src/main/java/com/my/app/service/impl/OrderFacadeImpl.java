package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.*;
import com.my.app.client.ProductWebClient;
import com.my.app.model.converter.FromResponseToDtoProductConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.UserDto;
import com.my.app.service.OrderFacade;
import com.my.app.service.OrderService;
import com.my.app.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductWebClient productWebClient;
    private final FromResponseToDtoProductConverter toDtoProductConverter;

    @Override
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        OrderDto order = orderService.getOrderById(id);
        final Set<String> ids = getProductsIds(List.of(order));
        if (ids.isEmpty()) {
            throw new ObjectNotFoundException("It should be at least 1 products id");
        }
        final Map<String, ProductResponse> productsMap = getProducts(ids);
        populateOrders(List.of(order), productsMap);

        return order;
    }

    @Override
    @NonNull
    public Collection<OrderDto> getAllOrders() {
        Collection<OrderDto> orders = orderService.getAllOrders();
        final Set<String> ids = getProductsIds(orders);
        if (ids.isEmpty()) {
            throw new ObjectNotFoundException("It should be at least 1 products id");
        }
        final Map<String, ProductResponse> productsMap = getProducts(ids);
        populateOrders(orders, productsMap);

        return orders;
    }

    @Transactional
    @Override
    @NonNull
    public OrderDto createOrder(@NonNull OrderDto order) {
        final Long userId = order.getUser().getId();
        final UserDto user = userService.getUserById(userId);
        order.setUser(user);

        final Map<String, ProductResponse> products = buyProducts(order.getOrderItems());
        populateOrders(List.of(order), products);

        OrderDto createdOrder = orderService.createOrder(order);
        populateOrders(List.of(createdOrder), products);
        return createdOrder;
    }

    private Map<String, ProductResponse> getProducts(Set<String> ids) {
        List<ProductRequest> products = ids.stream()
                .map(ProductRequest::new)
                .collect(toList());
        final ProductListRequest productsRequest = new ProductListRequest(products);

        return productWebClient
                .getAllProductsByIds(productsRequest)
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private Map<String, ProductResponse> buyProducts(List<OrderItemDto> orderItems) {
        List<OrderProductRequest> products = orderItems.stream()
                .map(orderItem -> {
                    OrderProductRequest orderProductRequest = new OrderProductRequest();
                    orderProductRequest.setId(orderItem.getProductId());
                    orderProductRequest.setCount(orderItem.getCount());
                    return orderProductRequest;
                })
                .collect(toList());

        final OrderProductListRequest productsRequest = new OrderProductListRequest(products);

        return productWebClient
                .buyProducts(productsRequest)
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private void populateOrders(Collection<OrderDto> orderDtoList, final Map<String, ProductResponse> productsMap) {
        for (OrderDto orderDto : orderDtoList) {
            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                ProductResponse productResponse = productsMap.get(orderItemDto.getProductId());
                orderItemDto.setProduct(toDtoProductConverter.convert(productResponse));
                orderItemDto.setCost(productResponse.getCost());
            }
        }
    }

    private Set<String> getProductsIds(Collection<OrderDto> orders) {
        return orders.stream()
                .map(OrderDto::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItemDto::getProductId)
                .collect(toSet());
    }
}
