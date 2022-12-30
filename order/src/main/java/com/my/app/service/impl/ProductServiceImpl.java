package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.request.ProductRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.model.converter.FromResponseToDtoProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductClient productClient;
    private final FromResponseToDtoProductConverter toDtoProductConverter;

    @Override
    @NonNull
    public List<ProductDto> getAllProductsByIds(@NonNull Set<String> productIds) {
        final String productIdsRow = String.join(", ", productIds);
        log.info("Fetching products with (ids = {})", productIdsRow);

        final List<ProductRequest> listProductRequest = productIds.stream()
                .map(ProductRequest::new)
                .collect(toList());

        final ProductListRequest productsRequest = new ProductListRequest(listProductRequest);

        final List<ProductResponse> productsResponse = productClient.getAllProductsByIds(productsRequest);
        if (productsResponse == null) {
            log.warn("Products with (ids = {}) are not found", productIdsRow);
            throw new ObjectNotFoundException(String.format("Products with (ids = %s) are not found", productIdsRow));
        }

        log.info("Products with (id = {}) are fetched. Products: {}", productIdsRow, productsResponse);

        return productsResponse.stream()
                .map(toDtoProductConverter::convert)
                .collect(toList());
    }
}
