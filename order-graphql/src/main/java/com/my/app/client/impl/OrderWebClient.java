package com.my.app.client.impl;

import com.my.app.client.OrderClient;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.exception.ApiClientErrorException;
import com.my.app.model.response.OrderResponse;
import com.my.app.utils.SecurityUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderWebClient implements OrderClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @CircuitBreaker(name = "order", fallbackMethod = "orderFallback")
    @NonNull
    @Override
    public Optional<OrderResponse> getOrderById(@NonNull Long orderId) {
        final String uri = String.format(propertiesConfig.getOrdersHost() + "/api/v1/orders/%d/plain", orderId);

        log.info("Sending getOrderById request to Order Service. Order Service URI = {},  Order Id = {}",
                uri, orderId);

        return webClient
                .get()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(SecurityUtils.getAuthToken()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiClientErrorException()))
                .bodyToMono(OrderResponse.class)
                .onErrorResume(e -> e instanceof ApiClientErrorException, e -> Mono.empty())
                .blockOptional();
    }

    @CircuitBreaker(name = "order", fallbackMethod = "orderListFallback")
    @Override
    public List<OrderResponse> getAllOrdersByUserId(@NonNull Long userId) {
        final String uri = String.format(propertiesConfig.getOrdersHost() + "/api/v1/orders/user/%d/plain", userId);

        log.info("Sending getAllOrdersByUserId request to Order Service. Order Service URI = {},  UserId Id = {}",
                uri, userId);

        return webClient
                .get()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(SecurityUtils.getAuthToken()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiClientErrorException()))
                .bodyToMono(new ParameterizedTypeReference<List<OrderResponse>>() {
                })
                .onErrorReturn(e -> e instanceof ApiClientErrorException, Collections.emptyList())
                .block();
    }

    private Optional<OrderResponse> orderFallback(Exception ex) {
        log.warn("Returning order fallback method");
        return Optional.empty();
    }

    private List<OrderResponse> orderListFallback(Exception ex) {
        log.warn("Returning order list fallback method");
        return Collections.emptyList();
    }
}
