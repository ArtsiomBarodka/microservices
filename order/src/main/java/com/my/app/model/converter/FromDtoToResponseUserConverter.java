package com.my.app.model.converter;

import com.epam.app.model.Category;
import com.my.app.model.dto.*;
import com.my.app.model.response.OrderItemResponse;
import com.my.app.model.response.OrderResponse;
import com.my.app.model.response.ProductResponse;
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
        if (source.getCategory() == Category.LAPTOP && source instanceof LaptopDto) {
            final LaptopDto laptop = (LaptopDto) source;
            return ProductResponse.builder()
                    .id(laptop.getId())
                    .name(laptop.getName())
                    .description(laptop.getDescription())
                    .cost(laptop.getCost())
                    .category(laptop.getCategory())
                    .storage(laptop.getStorage())
                    .ram(laptop.getRam())
                    .processor(laptop.getProcessor())
                    .build();
        } else if (source.getCategory() == Category.SMARTPHONE && source instanceof SmartphoneDto) {
            final SmartphoneDto smartphone = (SmartphoneDto) source;
            return ProductResponse.builder()
                    .id(smartphone.getId())
                    .name(smartphone.getName())
                    .description(smartphone.getDescription())
                    .cost(smartphone.getCost())
                    .category(smartphone.getCategory())
                    .storage(smartphone.getStorage())
                    .bluetooth(smartphone.getHasBluetooth())
                    .build();
        } else {
            throw new RuntimeException("Unsupported product");
        }
    };
}
