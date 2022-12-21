package com.my.app.service;

import com.my.app.model.dto.ProductDto;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    @NonNull ProductDto getProductById(@NonNull Long id);

    @NonNull Collection<ProductDto> getAllProducts();

    @NonNull Collection<ProductDto> getAllProductsByIds(@NonNull Collection<String> ids);

    @NonNull Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> products);

    @NonNull Collection<ProductDto> addProductsCount(@NonNull List<ProductDto> products);

    @NonNull Collection<ProductDto> subtractProductsCount(@NonNull List<ProductDto> products);
}
