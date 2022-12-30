package com.my.app.service;

import com.my.app.model.dto.ProductDto;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface ProductService {
    @NonNull
    List<ProductDto> getAllProductsByIds(@NonNull Set<String> productIds);
}
