package com.my.app.model.converter;

import com.epam.app.model.response.ProductResponse;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.response.OrderItemResponse;
import com.my.app.model.response.OrderResponse;
import com.my.app.model.response.UserResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class FromDtoToResponseOrderConverter implements Converter<OrderDto, OrderResponse> {
    @Override
    @NotNull
    public OrderResponse convert(@NonNull OrderDto source) {
        List<OrderItemResponse> orderItems = source.getOrderItems()
                .stream()
                .map(FromDtoToResponseOrderConverter.orderItemConverter::convert)
                .collect(toList());

        return OrderResponse.builder()
                .id(source.getId())
                .created(source.getCreated())
                .orderStatus(source.getStatus())
                .totalCost(orderItems.stream()
                        .filter(Objects::nonNull)
                        .map(OrderItemResponse::getCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orderItems(orderItems)
                .user(FromDtoToResponseOrderConverter.userConverter.convert(source.getUser()))
                .build();
    }

    private static final Converter<OrderItemDto, OrderItemResponse> orderItemConverter = source -> {
        return OrderItemResponse.builder()
                .id(source.getId())
                .cost(source.getCost())
                .count(source.getCount())
                .product(FromDtoToResponseOrderConverter.productConverter.convert(source.getProduct()))
                .build();
    };

    private static final Converter<ProductDto, ProductResponse> productConverter = source -> {
        return ProductResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .category(source.getCategory())
                .hasBluetooth(source.getHasBluetooth())
                .processor(source.getProcessor())
                .ram(source.getRam())
                .storage(source.getStorage())
                .build();
    };

    private static final Converter<UserDto, UserResponse> userConverter = source -> {
        return UserResponse.builder()
                .id(source.getId())
                .email(source.getEmail())
                .name(source.getName())
                .build();
    };
}
