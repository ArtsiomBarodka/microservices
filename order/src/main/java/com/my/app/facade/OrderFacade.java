package com.my.app.facade;

import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderStatusDto;
import org.springframework.lang.NonNull;

import java.util.Collection;


public interface OrderFacade {
    @NonNull
    OrderDto getOrderById(@NonNull Long id, boolean isDetailed);

    @NonNull
    Collection<OrderDto> getAllOrdersByUserId(@NonNull Long id, boolean isDetailed);

    @NonNull
    OrderDto createOrder(@NonNull OrderDto order);

    @NonNull
    OrderStatusDto updateOrderStatus(@NonNull OrderStatusDto orderStatusDto);
}
