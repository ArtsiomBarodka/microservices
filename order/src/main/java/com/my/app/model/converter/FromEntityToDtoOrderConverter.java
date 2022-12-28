package com.my.app.model.converter;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.OrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class FromEntityToDtoOrderConverter implements Converter<Order, OrderDto> {
    @Override
    @NonNull
    public OrderDto convert(@NonNull Order source) {
        return OrderDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .orderItems(source.getOrderItems()
                        .stream()
                        .map(FromEntityToDtoOrderConverter.orderItemConverter::convert)
                        .collect(toList()))
                .user(new UserDto(source.getUserId()))
                .build();
    }

    private static final Converter<OrderItem, OrderItemDto> orderItemConverter = source -> {
        return OrderItemDto.builder()
                .id(source.getId())
                .product(new ProductDto(source.getProductId()))
                .cost(source.getCost())
                .count(source.getCount())
                .build();
    };
}
