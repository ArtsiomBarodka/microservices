package com.my.app.client;

import com.my.app.model.response.OrderResponse;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface OrderClient {
    @NonNull
    Optional<OrderResponse> getOrderById(@NonNull Long orderId);

    List<OrderResponse> getAllOrdersByUserId(@NonNull Long userId);
}
