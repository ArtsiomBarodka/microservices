package com.my.app.client.impl;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.response.CustomerResponse;
import com.my.app.client.CustomerClient;
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
public class CustomerWebClient implements CustomerClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @CircuitBreaker(name = "customer", fallbackMethod = "customerFallback")
    @Retry(name = "customer")
    @Override
    public Optional<CustomerResponse> subtractCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest) {
        final String uri = propertiesConfig.getCustomersHost() + "/api/v1/customers/funds/subtract";

        log.info("Sending subtractCustomerFund request to Customer Service. Customer Service URI = {},  Request: {}",
                uri, updateCustomerRequest);

        return webClient
                .patch()
                .uri(uri)
                .bodyValue(updateCustomerRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiRejectedException()))
                .bodyToMono(CustomerResponse.class)
                .onErrorResume(e -> e instanceof ApiRejectedException, e -> Mono.empty())
                .blockOptional();
    }

    @CircuitBreaker(name = "customer", fallbackMethod = "customerFallback")
    @Retry(name = "customer")
    @Override
    public Optional<CustomerResponse> addCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest) {
        final String uri = propertiesConfig.getCustomersHost() + "/api/v1/customers/funds/add";

        log.info("Sending addCustomerFund request to Customer Service. Customer Service URI = {},  Request: {}",
                uri, updateCustomerRequest);

        return webClient
                .patch()
                .uri(uri)
                .bodyValue(updateCustomerRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiRejectedException()))
                .bodyToMono(CustomerResponse.class)
                .onErrorResume(e -> e instanceof ApiRejectedException, e -> Mono.empty())
                .blockOptional();
    }

    private Optional<CustomerResponse> customerFallback(Exception ex) {
        log.warn("Returning customer fallback method");
        return Optional.empty();
    }
}
