package com.my.app.client.impl;

import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.exception.ApiClientErrorException;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductWebClient implements ProductClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @CircuitBreaker(name = "product", fallbackMethod = "productFallback")
    @Override
    public List<ProductResponse> getAllProductsByIds(@NonNull ProductListRequest productListRequest) {
        final String uri = propertiesConfig.getProductsHost() + "/api/v1/products/all";

        log.info("Sending getCustomerById request to Customer Service. Customer Service URI = {},  Request: {}",
                uri, productListRequest);

        return webClient
                .post()
                .uri(uri)
                .bodyValue(productListRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiClientErrorException()))
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .onErrorReturn(e -> e instanceof ApiClientErrorException, Collections.emptyList())
                .block();
    }

    private List<ProductResponse> productFallback(Exception ex) {
        log.warn("Returning product fallback method");
        return Collections.emptyList();
    }
}
