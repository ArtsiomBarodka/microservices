package com.my.app.model.converter;

import com.epam.app.model.Category;
import com.my.app.model.dto.*;
import com.my.app.model.view.OrderItemView;
import com.my.app.model.view.OrderView;
import com.my.app.model.view.ProductView;
import com.my.app.model.view.UserView;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class FromDtoToVewUserConverter implements Converter<UserDto, UserView> {
    @Override
    @NonNull
    public UserView convert(@NonNull UserDto source) {
        return UserView.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .orders(source.getOrders()
                        .stream()
                        .map(FromDtoToVewUserConverter.orderConverter::convert)
                        .collect(toList()))
                .build();
    }

    private static final Converter<OrderDto, OrderView> orderConverter = source -> {
        List<OrderItemView> orderItems = source.getOrderItems()
                .stream()
                .map(FromDtoToVewUserConverter.orderItemConverter::convert)
                .collect(toList());

        return OrderView.builder()
                .id(source.getId())
                .created(source.getCreated())
                .totalCost(orderItems.stream()
                        .map(OrderItemView::getProduct)
                        .map(ProductView::getCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orderItems(orderItems)
                .build();
    };

    private static final Converter<OrderItemDto, OrderItemView> orderItemConverter = source -> {
        return OrderItemView.builder()
                .id(source.getId())
                .count(source.getCount())
                .product(FromDtoToVewUserConverter.productConverter.convert(source.getProduct()))
                .build();
    };

    private static final Converter<ProductDto, ProductView> productConverter = source -> {
        if (source.getCategory() == Category.LAPTOP && source instanceof LaptopDto) {
            final LaptopDto laptop = (LaptopDto) source;
            return ProductView.builder()
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
            return ProductView.builder()
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