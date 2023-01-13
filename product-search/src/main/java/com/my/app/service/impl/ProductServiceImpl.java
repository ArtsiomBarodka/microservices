package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.entity.Product;
import com.my.app.repository.ProductRepository;
import com.my.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public void updateAllProducts(@NonNull List<ProductDto> newProductsFields) {
        final Set<String> ids = newProductsFields.stream()
                .map(ProductDto::getId)
                .collect(Collectors.toSet());

        final Iterable<Product> existingProducts = productRepository.findAllById(ids);

        final List<String> notFoundIds = new ArrayList<>(newProductsFields.size());
        for (ProductDto newProductFields : newProductsFields) {
            final Optional<Product> candidateForUpdate = StreamSupport.stream(existingProducts.spliterator(), false)
                    .filter(existingProduct -> existingProduct.getId().equals(newProductFields.getId()))
                    .findFirst();

            if (candidateForUpdate.isPresent()) {
                updateProduct(candidateForUpdate.get(), newProductFields);
            } else {
                notFoundIds.add(newProductFields.getId());
            }
        }
        if (!notFoundIds.isEmpty()) {
            log.warn("Products with (ids = {}) are not found", notFoundIds);
            throw new ObjectNotFoundException(String.format("Products with (ids = %s) are not found", String.join(", ", notFoundIds)));
        }

        final Iterable<Product> updatedProducts = productRepository.saveAll(existingProducts);
        log.info("Products with (ids = {}) are updated. Updated products: {}", ids, updatedProducts);
    }

    private void updateProduct(Product existingProduct, ProductDto newProductFields) {
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
        if (newProductFields.getHasBluetooth()) {
            existingProduct.setHasBluetooth(newProductFields.getHasBluetooth().toString());
        }
        if (newProductFields.getUpdated() != null) {
            existingProduct.setUpdated(newProductFields.getUpdated().toInstant(ZoneOffset.UTC));
        }
    }
}
