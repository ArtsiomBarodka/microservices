package com.my.app.client;

import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product", fallback = ProductFallback.class)
public interface ProductClient {

    @PostMapping("api/v1/products")
    List<ProductResponse> getAllProductsByIds(@RequestBody ProductListRequest productListRequest);
}