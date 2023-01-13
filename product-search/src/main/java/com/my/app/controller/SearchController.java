package com.my.app.controller;

import com.epam.app.model.response.ProductResponse;
import com.my.app.model.converter.FromDtoToResponseProductConverter;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products/search")
public class SearchController {
    private final SearchService searchService;
    private final FromDtoToResponseProductConverter toResponseProductConverter;

    @GetMapping
    public ResponseEntity<Collection<ProductResponse>> findProductsByQuery(@RequestParam(name = "query") @NotBlank String query) {
        final Collection<ProductDto> products = searchService.findBySearchQuery(query);

        final List<ProductResponse> convertedResult = products.stream()
                .map(toResponseProductConverter::convert)
                .collect(toList());
        log.info("All Products response for (query = {}). Products: {}", query, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }
}
