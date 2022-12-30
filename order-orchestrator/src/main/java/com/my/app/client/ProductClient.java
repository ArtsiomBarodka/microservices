package com.my.app.client;

import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.response.ProductResponse;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductClient {
    List<ProductResponse> subtractProductsCount(@NonNull UpdateProductListRequest updateProductListRequest);

    List<ProductResponse> addProductsCount(@NonNull UpdateProductListRequest updateProductListRequest);
}
