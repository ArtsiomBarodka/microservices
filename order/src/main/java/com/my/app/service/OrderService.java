package com.my.app.service;

import com.my.app.model.dto.OrderDto;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface OrderService {
    @NonNull
    OrderDto getOrderById(@NonNull Long id);

    @NonNull
    Collection<OrderDto> getAllOrdersByUserId(@NonNull Long id);

    @NonNull OrderDto createOrder(@NonNull OrderDto order);

    @NonNull OrderDto updateOrder(@NonNull OrderDto order);
}
