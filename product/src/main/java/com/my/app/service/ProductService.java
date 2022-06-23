package com.my.app.service;

import com.my.app.model.dto.ProductDto;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface ProductService {
    @NonNull Collection<ProductDto> getAllProducts();
    @NonNull Collection<ProductDto> getAllProductsByIds(@NonNull Collection<String> ids);
}
