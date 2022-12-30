package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.exception.ProcessException;
import com.my.app.model.UpdateOperationType;
import com.my.app.model.converter.FromEntityToDtoProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.entity.Product;
import com.my.app.repository.ProductRepository;
import com.my.app.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FromEntityToDtoProductConverter toDtoProductConverter;

    @Override
    @NonNull
    public ProductDto getProductById(@NonNull String id) {
        final Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with (id = {}) is not found", id);
                    return new ObjectNotFoundException(String.format("Product with (id = %s) is not found", id));
                });

        log.info("Product with (id = {}) is fetched. Product: {}", id, productEntity);

        return toDtoProductConverter.convert(productEntity);
    }

    @Override
    @NonNull
    public Collection<ProductDto> getAllProducts() {
        final Collection<Product> productEntities = productRepository.findAll();

        if (productEntities.isEmpty()) {
            log.warn("Products are not found");
            throw new ObjectNotFoundException("Products are not found");
        }

        log.info("All Products are fetched. Products: {}", productEntities);

        return productEntities.stream()
                .map(toDtoProductConverter::convert)
                .collect(toList());
    }

    @Override
    @NonNull
    public Collection<ProductDto> getAllProductsByIds(@NonNull Collection<String> ids) {
        final Iterable<Product> productEntities = productRepository.findAllById(ids);

        List<ProductDto> result = StreamSupport.stream(productEntities.spliterator(), false)
                .map(toDtoProductConverter::convert)
                .collect(toList());

        if (result.isEmpty()) {
            log.warn("Products with (ids = {}) are not found", ids);
            throw new ObjectNotFoundException(String.format("Products with (userId = %s) are not found", String.join(", ", ids)));
        }

        if (result.size() != ids.size()) {
            final String notFoundIdsRow = ids.stream()
                    .filter(id -> result.stream()
                            .map(ProductDto::getId)
                            .noneMatch(id::equals))
                    .collect(Collectors.joining(";"));

            log.warn("Products with (ids = {}) are not found", notFoundIdsRow);
            throw new ObjectNotFoundException(String.format("Products with (userId = %s) are not found", notFoundIdsRow));
        }

        log.info("Products with (ids = {}) are fetched. Products: {}", ids, productEntities);

        return result;
    }

    @Override
    @NonNull
    @Transactional
    public Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> newProductsFields, @NonNull UpdateOperationType updateOperationType) {
        final Set<String> ids = newProductsFields.stream().map(ProductDto::getId).collect(toSet());
        final Iterable<Product> existingProducts = productRepository.findAllById(ids);

        final List<String> notFoundIds = new ArrayList<>(newProductsFields.size());

        for (ProductDto newProductFields : newProductsFields) {
            final Optional<Product> candidateForUpdate = StreamSupport.stream(existingProducts.spliterator(), false)
                    .filter(existingProduct -> existingProduct.getId().equals(newProductFields.getId()))
                    .findFirst();

            if (candidateForUpdate.isPresent()) {
                updateProduct(candidateForUpdate.get(), newProductFields, updateOperationType);
            } else {
                notFoundIds.add(newProductFields.getId());
            }
        }

        if (!notFoundIds.isEmpty()) {
            log.warn("Products with (ids = {}) are not found", notFoundIds);
            throw new ObjectNotFoundException(String.format("Products with (ids = %s) are not found", String.join(", ", notFoundIds)));
        }

        log.info("Products with (ids = {}) are updated. Updated products: {}", ids, existingProducts);

        return StreamSupport.stream(existingProducts.spliterator(), false)
                .map(toDtoProductConverter::convert)
                .collect(toList());
    }

    private void updateProduct(Product existingProduct, ProductDto newProductFields, UpdateOperationType updateOperationType) {
        if (newProductFields.getCount() != null) {
            switch (updateOperationType) {
                case ADD:
                    existingProduct.setCount(existingProduct.getCount() + newProductFields.getCount());
                    break;

                case SUBTRACT:
                    int newCount = existingProduct.getCount() - newProductFields.getCount();
                    if (newCount < 0) {
                        log.warn("Product count can't be less than 0. New count = {}, Product id = {} Product count = {}",
                                newCount, existingProduct.getId(), existingProduct.getCount());

                        throw new ProcessException(String.format(
                                "Product count can't be less than 0. New count = %d, Product id = %s Product count = %d",
                                newCount, existingProduct.getId(), existingProduct.getCount()));
                    }
                    existingProduct.setCount(newCount);
                    break;

                case UPDATE:
                    updateProductFields(existingProduct, newProductFields);
                    break;
            }
        }
    }

    private void updateProductFields(Product existingProduct, ProductDto newProductFields) {
        if (newProductFields.getName() != null) {
            existingProduct.setName(newProductFields.getName());
        }
        if (newProductFields.getCount() != null) {
            existingProduct.setCount(newProductFields.getCount());
        }
        if (newProductFields.getDescription() != null) {
            existingProduct.setDescription(newProductFields.getDescription());
        }
        if (newProductFields.getCost() != null) {
            existingProduct.setCost(newProductFields.getCost());
        }
        if (newProductFields.getCategory() != null) {
            existingProduct.setCategory(newProductFields.getCategory());
        }
        if (newProductFields.getStorage() != null) {
            existingProduct.setStorage(newProductFields.getStorage());
        }
        if (newProductFields.getRam() != null) {
            existingProduct.setRam(newProductFields.getRam());
        }
        if (newProductFields.getProcessor() != null) {
            existingProduct.setProcessor(newProductFields.getProcessor());
        }
        if (newProductFields.getHasBluetooth() != null) {
            existingProduct.setHasBluetooth(newProductFields.getHasBluetooth());
        }
    }
}
