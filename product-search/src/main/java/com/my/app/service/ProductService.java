package com.my.app.service;

import com.my.app.model.dto.ProductDto;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductService {
    void updateAllProducts(@NonNull List<ProductDto> products);
}
