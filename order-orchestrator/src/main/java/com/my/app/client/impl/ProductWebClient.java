package com.my.app.client.impl;

import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductWebClient implements ProductClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    @Override
    public List<ProductResponse> subtractProductsCount(@NonNull UpdateProductListRequest updateProductListRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getProductsHost() + "/api/v1/products/count/subtract")
                .bodyValue(updateProductListRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .onErrorReturn(Collections.emptyList())
                .block();
    }

    @Override
    public List<ProductResponse> addProductsCount(@NonNull UpdateProductListRequest updateProductListRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getProductsHost() + "/api/v1/products/count/add")
                .bodyValue(updateProductListRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .onErrorReturn(Collections.emptyList())
                .block();
    }
}
