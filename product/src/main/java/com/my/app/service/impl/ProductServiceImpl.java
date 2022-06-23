package com.my.app.service.impl;

import com.my.app.model.converter.FromEntityToDtoProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.entity.Product;
import com.my.app.repository.ProductRepository;
import com.my.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FromEntityToDtoProductConverter productConverter;

    @Override
    @NonNull
    public Collection<ProductDto> getAllProducts() {
        final Collection<Product> productEntities = productRepository.findAll();

        return productEntities.stream()
                .map(productConverter::convert)
                .collect(toList());
    }

    @Override
    @NonNull
    public Collection<ProductDto> getAllProductsByIds(@NonNull Collection<String> ids) {
        final Iterable<Product> productEntities = productRepository.findAllById(ids);

        return StreamSupport.stream(productEntities.spliterator(), false)
                .map(productConverter::convert)
                .collect(toList());
    }
}
