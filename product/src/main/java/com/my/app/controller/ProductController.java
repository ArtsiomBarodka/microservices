package com.my.app.controller;

import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.request.ProductRequest;
import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.facade.ProductFacade;
import com.my.app.model.UpdateOperationType;
import com.my.app.model.converter.FromDtoToResponseProductConverter;
import com.my.app.model.converter.FromRequestToDtoOrderProductListConverter;
import com.my.app.model.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private ProductFacade productFacade;
    private FromDtoToResponseProductConverter toResponseProductConverter;
    private FromRequestToDtoOrderProductListConverter toDtoProductConverter;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable @NotBlank String id) {
        final ProductDto product = productFacade.getProductById(id);

        final ProductResponse convertedResult = toResponseProductConverter.convert(product);
        log.info("Product response for (id = {}). Product: {}", id, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @GetMapping
    public ResponseEntity<Collection<ProductResponse>> getAllProducts() {
        final Collection<ProductDto> products = productFacade.getAllProducts();

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("All Products response. Products: {}", convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer')")
    @PostMapping("/all")
    public ResponseEntity<Collection<ProductResponse>> getAllProductsByIds(@RequestBody @Valid ProductListRequest productsRequest) {
        final List<String> ids = productsRequest.getProducts().stream().map(ProductRequest::getId).collect(toList());

        final Collection<ProductDto> products = productFacade.getAllProductsByIds(ids);

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("Products response for (ids = {}). Products: {}", ids, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer')")
    @PatchMapping("/operation/count/subtract")
    public ResponseEntity<Collection<ProductResponse>> operationSubtractProductsCount(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productFacade.updateAllProducts(toDtoProductConverter.convert(updateProductListRequest), UpdateOperationType.SUBTRACT);

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("Updated Products operation response after subtracting count for (request = {}) . Updated Products: {}", updateProductListRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('customer')")
    @PatchMapping("/operation/count/add")
    public ResponseEntity<Collection<ProductResponse>> operationAddProductsCount(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productFacade.updateAllProducts(toDtoProductConverter.convert(updateProductListRequest), UpdateOperationType.ADD);

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("Updated Products operation response after adding count for (request = {}) . Updated Products: {}", updateProductListRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('owner')")
    @PatchMapping("/count/subtract")
    public ResponseEntity<Collection<ProductResponse>> subtractProductsCount(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productFacade.updateAllProducts(toDtoProductConverter.convert(updateProductListRequest), UpdateOperationType.SUBTRACT);

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("Updated Products response after subtracting count for (request = {}) . Updated Products: {}", updateProductListRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('owner')")
    @PatchMapping("/count/add")
    public ResponseEntity<Collection<ProductResponse>> addProductsCount(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productFacade.updateAllProducts(toDtoProductConverter.convert(updateProductListRequest), UpdateOperationType.ADD);

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("Updated Products response after adding count for (request = {}) . Updated Products: {}", updateProductListRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @PreAuthorize("hasRole('owner')")
    @PatchMapping
    public ResponseEntity<Collection<ProductResponse>> updateAllProductsInfo(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productFacade.updateAllProducts(toDtoProductConverter.convert(updateProductListRequest), UpdateOperationType.UPDATE);

        final List<ProductResponse> convertedResult = products.stream().map(toResponseProductConverter::convert).collect(toList());
        log.info("Updated Products response for (request = {}) . Updated Products: {}", updateProductListRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }
}
