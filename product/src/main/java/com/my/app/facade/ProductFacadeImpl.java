package com.my.app.facade;

import com.my.app.model.UpdateOperationType;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.ProductNotificationService;
import com.my.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {
    private final ProductService productService;
    private final ProductNotificationService productNotificationService;

    @Override
    @NonNull
    public ProductDto getProductById(@NonNull String id) {
        return productService.getProductById(id);
    }

    @Override
    @NonNull
    public Collection<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    @NonNull
    public Collection<ProductDto> getAllProductsByIds(@NonNull Collection<String> ids) {
        return productService.getAllProductsByIds(ids);
    }

    @Override
    @NonNull
    public Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> updateProducts, @NonNull UpdateOperationType updateOperationType) {
        final Collection<ProductDto> updatedProductDtoList = productService.updateAllProducts(updateProducts, updateOperationType);
        productNotificationService.notify(updatedProductDtoList);
        return updatedProductDtoList;
    }
}
