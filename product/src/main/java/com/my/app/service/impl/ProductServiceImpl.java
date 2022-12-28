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

        List<ProductDto> result = StreamSupport.stream(productEntities.spliterator(), false)
                .map(productConverter::convert)
                .collect(toList());

        if (result.size() != ids.size()) {
            throw new ObjectNotFoundException("Some of Products are not found");
        }

        return result;
    }

    @Override
    @NonNull
    @Transactional
    public Collection<ProductDto> updateAllProducts(@NonNull List<ProductDto> updateProducts, @NonNull UpdateOperationType updateOperationType) {
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

    private void updateProduct(Product existingProduct, ProductDto newProductFields, UpdateOperationType updateOperationType) {
        if (newProductFields.getCount() != null) {
            switch (updateOperationType) {
                case ADD:
                    existingProduct.setCount(existingProduct.getCount() + newProductFields.getCount());
                    break;

                case SUBTRACT:
                    int newCount = existingProduct.getCount() - newProductFields.getCount();
                    if (newCount < 0) {
                        throw new ProcessException("New count is less than 0");
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
