package com.my.app.model.converter;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.OrderItem;
import com.my.app.model.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class FromEntityToDtoUserConverter implements Converter<User, UserDto> {
    @Override
    @NonNull
    public UserDto convert(@NonNull User source) {
        return UserDto.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .orders(source.getOrders()
                        .stream()
                        .map(FromEntityToDtoUserConverter.orderConverter::convert)
                        .collect(toList()))
                .build();
    }

    private static final Converter<Order, OrderDto> orderConverter = source -> {
        return OrderDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .orderItems(source.getOrderItems()
                        .stream()
                        .map(FromEntityToDtoUserConverter.orderItemConverter::convert)
                        .collect(toList()))
                .build();
    };

    private static final Converter<OrderItem, OrderItemDto> orderItemConverter = source -> {
        return OrderItemDto.builder()
                .id(source.getId())
                .productId(source.getProduct().getProductId())
                .cost(source.getCost())
                .count(source.getCount())
                .build();
    };
}
