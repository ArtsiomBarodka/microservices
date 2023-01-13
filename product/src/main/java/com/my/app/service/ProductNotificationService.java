package com.my.app.service;

import com.my.app.model.dto.ProductDto;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface ProductNotificationService {
    void notify(@NonNull Collection<ProductDto> productDtoList);
}
