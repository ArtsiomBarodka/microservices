package com.my.app.client;

import com.epam.app.model.OrderProductListRequest;
import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductResponse;
import com.my.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductWebClient {
    private final PropertiesConfig propertiesConfig;
    private final WebClient webClient;

    public List<ProductResponse> getAllProductsByIds(ProductListRequest productListRequest) {
        return webClient
                .post()
                .uri(propertiesConfig.getProductsHost() + "/api/v1/products")
                .bodyValue(productListRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .block();
    }

    public List<ProductResponse> buyProducts(OrderProductListRequest orderProductListRequest) {
        return webClient
                .patch()
                .uri(propertiesConfig.getProductsHost() + "/api/v1/products")
                .bodyValue(orderProductListRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>() {
                })
                .block();
    }
}
