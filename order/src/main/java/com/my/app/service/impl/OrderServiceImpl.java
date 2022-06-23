package com.my.app.service.impl;

import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.model.converter.FromEntityToDtoOrderConverter;
import com.my.app.model.converter.FromResponseToDtoProductConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.OrderItem;
import com.my.app.model.entity.ProductId;
import com.my.app.repository.OrderRepository;
import com.my.app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final FromEntityToDtoOrderConverter orderConverter;
    private final FromResponseToDtoProductConverter productConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        final Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order is not founded"));

        final Set<String> ids = getProductsIds(List.of(orderEntity));

        final Map<String, ProductResponse> productsMap = getProducts(ids);

        final OrderDto orderDto = orderConverter.convert(orderEntity);

        populateOrders(List.of(orderDto), productsMap);

        return orderDto;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<OrderDto> getAllOrders() {
        final List<Order> orderEntities = orderRepository.findAll();

        final Set<String> ids = getProductsIds(orderEntities);

        final Map<String, ProductResponse> productsMap = getProducts(ids);

        final List<OrderDto> orderDtoList = orderEntities.stream()
                .map(orderConverter::convert)
                .collect(toList());

        populateOrders(orderDtoList, productsMap);

        return orderDtoList;
    }

    private Set<String> getProductsIds(List<Order> orderEntities) {
        return orderEntities.stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getProduct)
                .map(ProductId::getProductId)
                .collect(toSet());
    }

    private Map<String, ProductResponse> getProducts(Set<String> ids) {
        final ProductListRequest productListRequest = ProductListRequest.builder().ids(ids).build();

        return productClient
                .getAllProductsByIds(productListRequest)
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private void populateOrders(List<OrderDto> orderDtoList, final Map<String, ProductResponse> productsMap) {
        for (OrderDto orderDto : orderDtoList) {
            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                orderItemDto.setProduct(productConverter.convert(productsMap.get(orderItemDto.getProductId())));
            }
        }
    }
}
