package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.model.converter.FromEntityToDtoProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.entity.Product;
import com.my.app.repository.ProductRepository;
import com.my.app.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private static final String SPACE = " ";

    private final ProductRepository productRepository;
    private final FromEntityToDtoProductConverter toDtoProductConverter;

    @NonNull
    @Override
    public List<ProductDto> findBySearchQuery(@NonNull String query) {
        final List<Product> products;
        if (query.contains(SPACE)) {
            products = productRepository.findByNameOrDescriptionOrCategoryOrStorageOrRamOrProcessor(
                    query, query, query, query, query, query);
        } else {
            products = productRepository.findByNameLikeOrDescriptionLikeOrCategoryLikeOrStorageLikeOrRamLikeOrProcessorLike(
                    query, query, query, query, query, query);
        }

        if (products.isEmpty()) {
            log.warn("Products searched by (query = {}) are not found", query);
            throw new ObjectNotFoundException(String.format("Products searched by (query = %s) are not found", query));
        }

        log.info("Products searched by (query = {}) are found. Products: {}", query, products);
        return products.stream()
                .map(toDtoProductConverter::convert)
                .collect(Collectors.toList());
    }
}
