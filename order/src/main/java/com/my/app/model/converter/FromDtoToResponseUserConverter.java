package com.my.app.model.converter;

import com.epam.app.model.ProductResponse;
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

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class FromDtoToResponseUserConverter implements Converter<UserDto, UserResponse> {
    @Override
    @NonNull
    public UserResponse convert(@NonNull UserDto source) {
        return UserResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .orders(source.getOrders()
                        .stream()
                        .map(FromDtoToResponseUserConverter.orderConverter::convert)
                        .collect(toList()))
                .build();
    }

    private static final Converter<OrderDto, OrderResponse> orderConverter = source -> {
        List<OrderItemResponse> orderItems = source.getOrderItems()
                .stream()
                .map(FromDtoToResponseUserConverter.orderItemConverter::convert)
                .collect(toList());

        return OrderResponse.builder()
                .id(source.getId())
                .created(source.getCreated())
                .totalCost(orderItems.stream()
                        .map(OrderItemResponse::getProduct)
                        .map(ProductResponse::getCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orderItems(orderItems)
                .build();
    };

    private static final Converter<OrderItemDto, OrderItemResponse> orderItemConverter = source -> {
        return OrderItemResponse.builder()
                .id(source.getId())
                .count(source.getCount())
                .product(FromDtoToResponseUserConverter.productConverter.convert(source.getProduct()))
                .build();
    };

    private static final Converter<ProductDto, ProductResponse> productConverter = source -> {
        return ProductResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .category(source.getCategory())
                .cost(source.getCost())
                .hasBluetooth(source.getHasBluetooth())
                .processor(source.getProcessor())
                .ram(source.getRam())
                .storage(source.getStorage())
                .build();
    };
}
