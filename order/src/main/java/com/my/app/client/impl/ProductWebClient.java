package com.my.app.client.impl;

import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductWebClient implements ProductClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    public List<ProductResponse> getAllProductsByIds(@NonNull ProductListRequest productListRequest) {
        return webClient
                .post()
                .uri(propertiesConfig.getProductsHost() + "/api/v1/products/all")
                .bodyValue(productListRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .block();
    }


    public List<ProductResponse> subtractProductsCount(@NonNull UpdateProductListRequest updateProductListRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getProductsHost() + "/api/v1/products/count/subtract")
                .bodyValue(updateProductListRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .block();
    }
}
