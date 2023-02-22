package com.my.app.model.converter;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.response.OrderItemResponse;
import com.my.app.model.response.OrderResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class FromResponseToDtoOrderConverter implements Converter<OrderResponse, OrderDto> {
    @Override
    @NonNull
    public OrderDto convert(@NonNull OrderResponse source) {
        return OrderDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .status(source.getOrderStatus())
                .totalCost(source.getTotalCost())
                .orderItems(source.getOrderItems()
                        .stream()
                        .map(FromResponseToDtoOrderConverter.orderItemConverter::convert)
                        .collect(toList()))
                .user(new UserDto(source.getUser().getId()))
                .build();
    }

    private static final Converter<OrderItemResponse, OrderItemDto> orderItemConverter = source -> {
        return OrderItemDto.builder()
                .id(source.getId())
                .product(new ProductDto(source.getProduct().getId()))
                .cost(source.getCost())
                .count(source.getCount())
                .build();
    };
}
