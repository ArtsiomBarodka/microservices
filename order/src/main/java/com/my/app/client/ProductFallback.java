package com.my.app.client;

import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductFallback implements ProductClient{
    @Override
    public List<ProductResponse> getAllProductsByIds(ProductListRequest productListRequest) {
        return Collections.emptyList();
    }
}
