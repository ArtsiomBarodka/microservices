package com.my.app.client.impl;

import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.exception.ApiRejectedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductWebClient implements ProductClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @CircuitBreaker(name = "product", fallbackMethod = "productFallback")
    @Retry(name = "product")
    @Override
    public List<ProductResponse> subtractProductsCount(@NonNull UpdateProductListRequest updateProductListRequest, @NonNull String token) {
        final String uri = propertiesConfig.getProductsHost() + "/api/v1/products/operation/count/subtract";

        log.info("Sending subtractProductsCount request to Product Service. Product Service URI = {},  Request: {}",
                uri, updateProductListRequest);

        return webClient
                .patch()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(token))
                .bodyValue(updateProductListRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiRejectedException()))
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .onErrorReturn(e -> e instanceof ApiRejectedException, Collections.emptyList())
                .block();
    }

    @CircuitBreaker(name = "product", fallbackMethod = "productFallback")
    @Retry(name = "product")
    @Override
    public List<ProductResponse> addProductsCount(@NonNull UpdateProductListRequest updateProductListRequest, @NonNull String token) {
        final String uri = propertiesConfig.getProductsHost() + "/api/v1/products/operation/count/add";

        log.info("Sending addProductsCount request to Product Service. Product Service URI = {},  Request: {}",
                uri, updateProductListRequest);

        return webClient
                .patch()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(token))
                .bodyValue(updateProductListRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiRejectedException()))
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .onErrorReturn(e -> e instanceof ApiRejectedException, Collections.emptyList())
                .block();
    }

    private List<ProductResponse> productFallback(Exception ex) {
        log.warn("Returning product fallback method");
        return Collections.emptyList();
    }
}
