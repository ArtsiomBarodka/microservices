package com.my.app.model.converter;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.request.OrderRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FromRequestToDtoOrderConverter implements Converter<OrderRequest, OrderDto> {
    @Override
    @NonNull
    public OrderDto convert(@NonNull OrderRequest source) {
        return OrderDto.builder()
                .user(convertToUserDto(source))
                .orderItems(convertToOrderItemDto(source))
                .build();
    }

    private UserDto convertToUserDto(OrderRequest source) {
        return UserDto.builder()
                .id(source.getUserId())
                .build();
    }

    private List<OrderItemDto> convertToOrderItemDto(OrderRequest source) {
        return source.getItems().stream().map(productRequest -> {
            return OrderItemDto.builder()
                    .productId(productRequest.getProductId())
                    .count(productRequest.getCount())
                    .build();
        }).collect(Collectors.toList());
    }
}
