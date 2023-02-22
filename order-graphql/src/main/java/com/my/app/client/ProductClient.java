package com.my.app.client;

import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.response.ProductResponse;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductClient {
    List<ProductResponse> getAllProductsByIds(@NonNull ProductListRequest productListRequest);
}
