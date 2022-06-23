package com.my.app.service.impl;

import com.epam.app.model.Laptop;
import com.epam.app.model.Product;
import com.epam.app.model.Smartphone;
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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final FromEntityToDtoOrderConverter orderConverter;
    private final FromResponseToDtoProductConverter productConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        final Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order is not founded"));

        final Map<Long, Product> products = orderEntity.getOrderItems()
                .stream()
                .map(OrderItem::getProduct)
                .map(ProductId::getProductId)
                .map(this::fetchProduct)
                .collect(toMap(Product::getId, Function.identity(), (x1, x2) -> x1));

        final OrderDto orderDto = orderConverter.convert(orderEntity);

        populateOrders(List.of(orderDto), products);

        return orderDto;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<OrderDto> getAllOrders() {
        final List<Order> orderEntities = orderRepository.findAll();

        final Map<Long, Product> productsMap = orderEntities.stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getProduct)
                .map(ProductId::getProductId)
                .map(this::fetchProduct)
                .collect(toMap(Product::getId, Function.identity(), (x1, x2) -> x1));

        final List<OrderDto> orderDtoList = orderEntities.stream().map(orderConverter::convert).collect(toList());

        populateOrders(orderDtoList, productsMap);

        return orderDtoList;
    }

    private void populateOrders(List<OrderDto> orderDtoList, final Map<Long, Product> productsMap) {
        for (OrderDto orderDto : orderDtoList) {
            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                orderItemDto.setProduct(productConverter.convert(productsMap.get(orderItemDto.getProductId())));
            }
        }
    }

    private Product fetchProduct(Long id) {
        final long random = Math.round(Math.random());
        if (1 == random) {
            return Laptop.builder()
                    .id(id)
                    .name("Macbook PRO")
                    .description("ultrabook for professionals")
                    .cost(BigDecimal.valueOf(5500))
                    .processor("Intel Core i7")
                    .ram("16 Gb")
                    .storage("500 Gb")
                    .build();
        } else {
            return Smartphone.builder()
                    .id(id)
                    .name("Iphone X")
                    .description("smartphone for all")
                    .cost(BigDecimal.valueOf(3500))
                    .storage("500 Gb")
                    .hasBluetooth(true)
                    .build();
        }
    }
}
