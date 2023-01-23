package com.my.app.service;

import com.my.app.model.dto.OrderDto;
import org.springframework.lang.NonNull;

public interface OrderService {
    void processOrder(@NonNull OrderDto orderDto, @NonNull String token);
}
