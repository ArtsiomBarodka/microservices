package com.my.app.controller;

import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.request.ProductRequest;
import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.UpdateOperationType;
import com.my.app.model.converter.FromDtoToResponseProductConverter;
import com.my.app.model.converter.FromRequestToDtoOrderProductListConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private PropertiesConfig propertiesConfig;
    private ProductService productService;
    private FromDtoToResponseProductConverter productDtoConverter;
    private FromRequestToDtoOrderProductListConverter productRequestConverter;

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello from " + propertiesConfig.getName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable @NotNull @Min(1) Long id) {
        final ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(productDtoConverter.convert(product));
    }

    @GetMapping
    public ResponseEntity<Collection<ProductResponse>> getAllProducts() {
        final Collection<ProductDto> products = productService.getAllProducts();

        return ResponseEntity.ok(products.stream()
                .map(productDtoConverter::convert)
                .collect(toList()));
    }

    @PostMapping("/all")
    public ResponseEntity<Collection<ProductResponse>> getAllProductsByIds(@RequestBody @Valid ProductListRequest productsRequest) {
        final List<String> ids = productsRequest.getProducts().stream()
                .map(ProductRequest::getId).
                collect(toList());

        final Collection<ProductDto> products = productService.getAllProductsByIds(ids);

        return ResponseEntity.ok(products.stream()
                .map(productDtoConverter::convert)
                .collect(toList()));
    }

    @PatchMapping("/count/subtract")
    public ResponseEntity<Collection<ProductResponse>> subtractProductsCount(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productService.updateAllProducts(productRequestConverter.convert(updateProductListRequest), UpdateOperationType.SUBTRACT);

        return ResponseEntity.ok(products.stream()
                .map(productDtoConverter::convert)
                .collect(toList()));
    }

    @PatchMapping("/count/add")
    public ResponseEntity<Collection<ProductResponse>> addProductsCount(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productService.updateAllProducts(productRequestConverter.convert(updateProductListRequest), UpdateOperationType.ADD);

        return ResponseEntity.ok(products.stream()
                .map(productDtoConverter::convert)
                .collect(toList()));
    }

    @PatchMapping
    public ResponseEntity<Collection<ProductResponse>> updateAllProductsInfo(@RequestBody @Valid UpdateProductListRequest updateProductListRequest) {
        final Collection<ProductDto> products = productService.updateAllProducts(productRequestConverter.convert(updateProductListRequest), UpdateOperationType.UPDATE);

        return ResponseEntity.ok(products.stream()
                .map(productDtoConverter::convert)
                .collect(toList()));
    }
}
