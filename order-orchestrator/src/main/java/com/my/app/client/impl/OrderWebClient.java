package com.my.app.client.impl;

import com.epam.app.model.request.UpdateOrderStatusRequest;
import com.epam.app.model.response.OrderStatusResponse;
import com.my.app.client.OrderClient;
import com.my.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrderWebClient implements OrderClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @Override
    public Optional<OrderStatusResponse> updateOrderStatus(@NonNull UpdateOrderStatusRequest updateOrderStatusRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getOrdersHost() + "/api/v1/orders/status")
                .bodyValue(updateOrderStatusRequest)
                .retrieve()
                .bodyToMono(OrderStatusResponse.class)
                .onErrorResume((e) -> Mono.empty())
                .blockOptional();
    }
}
