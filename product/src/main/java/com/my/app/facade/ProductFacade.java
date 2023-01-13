package com.my.app.facade;

import com.my.app.model.UpdateOperationType;
import com.my.app.model.dto.ProductDto;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface ProductFacade {
    @NonNull
    ProductDto getProductById(@NonNull String id);

    @NonNull
    Collection<ProductDto> getAllProducts();

    @NonNull
    Collection<ProductDto> getAllProductsByIds(@NonNull Collection<String> ids);

    @NonNull
    Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> updateProducts, @NonNull UpdateOperationType updateOperationType);
}
