package com.my.app.client.impl;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.response.CustomerResponse;
import com.my.app.client.CustomerClient;
import com.my.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomerWebClient implements CustomerClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @Override
    public Optional<CustomerResponse> subtractCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getCustomersHost() + "/api/v1/customers/funds/subtract")
                .bodyValue(updateCustomerRequest)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .onErrorResume((e) -> Mono.empty())
                .blockOptional();
    }

    @Override
    public Optional<CustomerResponse> addCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getCustomersHost() + "/api/v1/customers/funds/add")
                .bodyValue(updateCustomerRequest)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .onErrorResume((e) -> Mono.empty())
                .blockOptional();
    }
}
