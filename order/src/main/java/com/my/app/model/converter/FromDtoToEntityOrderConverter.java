package com.my.app.model.converter;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.OrderItem;
import com.my.app.model.entity.ProductId;
import com.my.app.model.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class FromDtoToEntityOrderConverter implements Converter<OrderDto, Order> {
    @Override
    @NonNull
    public Order convert(@NonNull OrderDto source) {
        List<OrderItem> orderItems = source.getOrderItems()
                .stream()
                .map(this::orderItemConverter)
                .collect(toList());

        Order order = new Order();
        order.addOrderItems(orderItems);
        order.setUser(userConverter(source.getUser()));

        return order;
    }

    private OrderItem orderItemConverter(OrderItemDto source) {
        if (source == null) {
            return null;
        }

        return OrderItem.builder()
                .product(ProductId.builder()
                            .productId(source.getProduct().getId())
                            .build())
                .cost(source.getCost())
                .count(source.getCount())
                .build();
    }

    private User userConverter(UserDto source) {
        if (source == null) {
            return null;
        }

        User user = User.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .build();

        return user;
    }
}
