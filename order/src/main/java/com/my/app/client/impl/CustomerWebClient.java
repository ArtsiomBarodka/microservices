package com.my.app.client.impl;

import com.epam.app.model.response.CustomerResponse;
import com.my.app.client.CustomerClient;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.exception.ApiClientErrorException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @NonNull
    @Override
    public Optional<CustomerResponse> getCustomerById(@NonNull Long id) {
        final String uri = String.format(propertiesConfig.getCustomersHost() + "/api/v1/customers/%d", id);

        log.info("Sending getCustomerById request to Customer Service. Customer Service URI = {},  Customer Id = {}",
                uri, id);

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiClientErrorException()))
                .bodyToMono(CustomerResponse.class)
                .onErrorResume(e -> e instanceof ApiClientErrorException, e -> Mono.empty())
                .blockOptional();
    }

    private Optional<CustomerResponse> customerFallback(Exception ex) {
        log.warn("Returning customer fallback method");
        return Optional.empty();
    }
}
