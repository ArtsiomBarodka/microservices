package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.model.converter.FromEntityToDtoProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.UpdateOperationType;
import com.my.app.model.entity.Product;
import com.my.app.service.ProductService;
import com.my.app.service.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FromEntityToDtoProductConverter productConverter;

    @Override
    @NonNull
    public ProductDto getProductById(@NonNull Long id) {
        final Product productEntity = productRepository.findById(id.toString()).orElseThrow(() -> new ObjectNotFoundException("Product is not found"));

        return productConverter.convert(productEntity);
    }

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

    @Override
    @NonNull
    @Transactional
    public Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> updateProducts) {
        return updateAllProducts(updateProducts, UpdateOperationType.UPDATE);
    }

    @Override
    @NonNull
    @Transactional
    public Collection<ProductDto> addProductsCount(@NonNull List<ProductDto> updateProducts) {
        return updateAllProducts(updateProducts, UpdateOperationType.ADD);
    }

    @Override
    @NonNull
    @Transactional
    public Collection<ProductDto> subtractProductsCount(@NonNull List<ProductDto> updateProducts) {
        return updateAllProducts(updateProducts, UpdateOperationType.SUBTRACT);
    }

    public Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> updateProducts, UpdateOperationType updateOperationType) {
        final Set<String> ids = updateProducts.stream().map(ProductDto::getId).collect(toSet());
        final Iterable<Product> existingProducts = productRepository.findAllById(ids);


        for (Product existingProduct : existingProducts) {
            updateProducts.stream()
                    .filter(updateProduct -> existingProduct.getId().equals(updateProduct.getId()))
                    .findFirst()
                    .ifPresent(product -> updateProduct(existingProduct, product, updateOperationType));
        }

        final List<Product> newProducts = productRepository.saveAll(existingProducts);
        return newProducts.stream()
                .map(productConverter::convert)
                .collect(toList());
    }

    private void updateProduct(Product existingProduct, ProductDto updateProduct, UpdateOperationType updateOperationType) {
        if (updateProduct.getCount() != null) {
            switch (updateOperationType) {
                case ADD:
                    existingProduct.setCount(existingProduct.getCount() + updateProduct.getCount());
                    break;

                case SUBTRACT:
                    int newCount = existingProduct.getCount() - updateProduct.getCount();
                    if (newCount < 0) {
                        throw new RuntimeException("new count is less than 0");
                    }
                    existingProduct.setCount(newCount);
                    break;

                case UPDATE:
                    existingProduct.setCount(updateProduct.getCount());
                    break;
            }
        }

        if (UpdateOperationType.UPDATE == updateOperationType) {
            if (updateProduct.getName() != null) {
                existingProduct.setName(updateProduct.getName());
            }
            if (updateProduct.getDescription() != null) {
                existingProduct.setDescription(updateProduct.getDescription());
            }
            if (updateProduct.getCost() != null) {
                existingProduct.setCost(updateProduct.getCost());
            }
            if (updateProduct.getCategory() != null) {
                existingProduct.setCategory(updateProduct.getCategory());
            }
            if (updateProduct.getStorage() != null) {
                existingProduct.setStorage(updateProduct.getStorage());
            }
            if (updateProduct.getRam() != null) {
                existingProduct.setRam(updateProduct.getRam());
            }
            if (updateProduct.getProcessor() != null) {
                existingProduct.setProcessor(updateProduct.getProcessor());
            }
            if (updateProduct.getHasBluetooth() != null) {
                existingProduct.setHasBluetooth(updateProduct.getHasBluetooth());
            }
        }
    }
}
