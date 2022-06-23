package com.my.app.controller;

import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductResponse;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.converter.FromDtoToResponseProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private PropertiesConfig propertiesConfig;
    private ProductService productService;
    private FromDtoToResponseProductConverter productConverter;

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello from " + propertiesConfig.getName();
    }

    @GetMapping
    public ResponseEntity<Collection<ProductResponse>> getAllProducts(@RequestBody @Valid Optional<ProductListRequest> productListRequest) {
        final Collection<ProductDto> products = productListRequest
                .map(listRequest -> productService.getAllProductsByIds(listRequest.getIds()))
                .orElseGet(() -> productService.getAllProducts());

        return ResponseEntity.ok(products.stream()
                .map(productConverter::convert)
                .collect(toList()));
    }
}
