package com.my.app.client;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.response.CustomerResponse;
import com.my.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class CustomerWebClient implements CustomerClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @Override
    public CustomerResponse subtractCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getCustomersHost() + "/api/v1/customers/funds/subtract")
                .bodyValue(updateCustomerRequest)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .block();
    }

    @Override
    public CustomerResponse getCustomerById(@NonNull Long id) {
        return webClient
                .get()
                .uri(String.format(propertiesConfig.getCustomersHost() + "/api/v1/customers/%d", id))
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .block();
    }
}
