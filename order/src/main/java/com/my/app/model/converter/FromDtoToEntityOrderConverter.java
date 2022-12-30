package com.my.app.model.converter;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.OrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class FromDtoToEntityOrderConverter implements Converter<OrderDto, Order> {
    @Override
    @NonNull
    public Order convert(@NonNull OrderDto source) {
        List<OrderItem> orderItems = source.getOrderItems()
                .stream()
                .map(orderItemDto -> OrderItem.builder()
                        .count(orderItemDto.getCount())
                        .cost(orderItemDto.getCost())
                        .productId(orderItemDto.getProduct().getId())
                        .build())
                .collect(toList());

        return Order.builder()
                .userId(source.getUser().getId())
                .status(source.getStatus())
                .orderItems(orderItems)
                .build();
    }
}
