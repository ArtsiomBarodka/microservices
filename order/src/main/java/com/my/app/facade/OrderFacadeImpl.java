package com.my.app.facade;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.OrderStatusDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UserDto;
import com.my.app.service.OrderProcessNotificationService;
import com.my.app.service.OrderService;
import com.my.app.service.ProductService;
import com.my.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderProcessNotificationService orderProcessNotificationService;

    @Override
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        // fetching order
        OrderDto order = orderService.getOrderById(id);

        // fetching products
        final Map<String, ProductDto> productsMap = getProductsMap(List.of(order));
        populateOrdersByProducts(List.of(order), productsMap, false);

        //fetching user
        final UserDto user = userService.getUserById(order.getUser().getId());
        order.setUser(user);

        return order;
    }

    @Override
    @NonNull
    public Collection<OrderDto> getAllOrdersByUserId(@NonNull Long id) {
        // fetching orders
        Collection<OrderDto> orders = orderService.getAllOrdersByUserId(id);

        // fetching products
        final Map<String, ProductDto> productsMap = getProductsMap(orders);
        populateOrdersByProducts(orders, productsMap, false);

        //fetching user
        final UserDto user = userService.getUserById(id);
        orders.forEach(order -> order.setUser(user));

        return orders;
    }

    @Transactional
    @Override
    @NonNull
    public OrderDto createOrder(@NonNull OrderDto order) {
        // fetching customer info
        final UserDto user = userService.getUserById(order.getUser().getId());

        // fetching products info
        final Map<String, ProductDto> productsMap = getProductsMap(List.of(order));
        populateOrdersByProducts(List.of(order), productsMap, true);

        // creating new order
        final OrderDto savedNewOrder = orderService.createOrder(order);
        populateOrdersByProducts(List.of(savedNewOrder), productsMap, false);
        savedNewOrder.setUser(user);

        // starting order process
        orderProcessNotificationService.startOrderProcess(savedNewOrder);

        return savedNewOrder;
    }

    @Override
    @NonNull
    public OrderStatusDto updateOrderStatus(@NonNull OrderStatusDto orderStatusDto) {
        final OrderDto order = orderService.getOrderById(orderStatusDto.getOrderId());
        order.setStatus(orderStatusDto.getOrderStatus());
        final OrderDto updatedOrder = orderService.updateOrder(order);

        return new OrderStatusDto(updatedOrder.getId(), updatedOrder.getUser().getId(), updatedOrder.getStatus());
    }

    private Map<String, ProductDto> getProductsMap(Collection<OrderDto> orderDtoList) {
        final Set<String> ids = getProductsIds(orderDtoList);
        if (ids.isEmpty()) {
            final String ordersIdRow = orderDtoList.stream()
                    .map(OrderDto::getId)
                    .map(String::valueOf)
                    .collect(joining(","));

            log.error("It should be at least 1 products id for Orders with (ids = {})", ordersIdRow);
            throw new ObjectNotFoundException(String.format("It should be at least 1 products id for Orders with (ids = %s)", ordersIdRow));
        }

        final List<ProductDto> products = productService.getAllProductsByIds(ids);

        return products.stream()
                .collect(toMap(ProductDto::getId, Function.identity()));
    }

    private void populateOrdersByProducts(Collection<OrderDto> orderDtoList, final Map<String, ProductDto> productsMap, boolean useProductCost) {
        for (OrderDto orderDto : orderDtoList) {
            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                ProductDto product = productsMap.get(orderItemDto.getProduct().getId());
                if (product == null) {
                    log.error("Product with (productId = {}) is not found for Order with (id = {})",
                            orderItemDto.getProduct().getId(),
                            orderItemDto.getId());

                    throw new ObjectNotFoundException(String.format("Product with (productId = %s) is not found for Order with (id = %d)",
                            orderItemDto.getProduct().getId(),
                            orderItemDto.getId()));
                }

                if (useProductCost) {
                    orderItemDto.setCost(BigDecimal.valueOf(orderItemDto.getCount()).multiply(product.getCost()));
                }

                orderItemDto.setProduct(product);
            }
        }
    }

    private Set<String> getProductsIds(Collection<OrderDto> orders) {
        return orders.stream()
                .map(OrderDto::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItemDto::getProduct)
                .map(ProductDto::getId)
                .collect(toSet());
    }
}
