package com.my.app.client.impl;

import com.epam.app.model.request.UpdateOrderStatusRequest;
import com.epam.app.model.response.OrderStatusResponse;
import com.my.app.client.OrderClient;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.exception.ApiRejectedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderWebClient implements OrderClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @CircuitBreaker(name = "order", fallbackMethod = "orderFallback")
    @Retry(name = "order")
    @Override
    public Optional<OrderStatusResponse> updateOrderStatus(@NonNull UpdateOrderStatusRequest updateOrderStatusRequest, @NonNull String token) {
        final String uri = propertiesConfig.getOrdersHost() + "/api/v1/orders/status";

        log.info("Sending updateOrderStatus request to Order Service. Order Service URI = {},  Request: {}",
                uri, updateOrderStatusRequest);

        return webClient
                .patch()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(token))
                .bodyValue(updateOrderStatusRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiRejectedException()))
                .bodyToMono(OrderStatusResponse.class)
                .onErrorResume(e -> e instanceof ApiRejectedException, e -> Mono.empty())
                .blockOptional();
    }

    private Optional<OrderStatusResponse> orderFallback(Exception ex) {
        log.warn("Returning order fallback method");
        return Optional.empty();
    }
}
