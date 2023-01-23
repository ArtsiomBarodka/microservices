package com.my.app.client;

import com.epam.app.model.request.UpdateOrderStatusRequest;
import com.epam.app.model.response.OrderStatusResponse;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface OrderClient {
    Optional<OrderStatusResponse> updateOrderStatus(@NonNull UpdateOrderStatusRequest updateOrderStatusRequest, @NonNull String token);
}
