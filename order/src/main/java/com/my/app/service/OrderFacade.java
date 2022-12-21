package com.my.app.service;

import com.my.app.model.dto.OrderDto;
import org.springframework.lang.NonNull;

import java.util.Collection;


public interface OrderFacade {
    @NonNull
    OrderDto getOrderById(@NonNull Long id);

    @NonNull
    Collection<OrderDto> getAllOrders();
    @NonNull
    OrderDto createOrder(@NonNull OrderDto order);
}
