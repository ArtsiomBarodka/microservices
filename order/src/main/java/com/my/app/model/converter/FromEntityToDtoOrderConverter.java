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
public class FromEntityToDtoOrderConverter implements Converter<Order, OrderDto> {
    @Override
    public OrderDto convert(Order source) {
        if (source == null){
            return null;
        }

        return OrderDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .orderItems(source.getOrderItems()
                        .stream()
                        .map(FromEntityToDtoOrderConverter.orderItemConverter::convert)
                        .collect(toList()))
                .user(FromEntityToDtoOrderConverter.userConverter.convert(source.getUser()))
                .build();
    }

    private static final Converter<OrderItem, OrderItemDto> orderItemConverter = source -> {
        if (source == null){
            return null;
        }

        return OrderItemDto.builder()
                .id(source.getId())
                .productId(source.getProduct().getProductId())
                .cost(source.getCost())
                .count(source.getCount())
                .build();
    };

    private static final Converter<User, UserDto> userConverter = source -> {
        if (source == null){
            return null;
        }

        return UserDto.builder()
                .id(source.getId())
                .email(source.getEmail())
                .name(source.getName())
                .build();
    };
}
