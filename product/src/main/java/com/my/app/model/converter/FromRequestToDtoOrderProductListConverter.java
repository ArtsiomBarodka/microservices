package com.my.app.model.converter;

import com.epam.app.model.request.UpdateProductListRequest;
import com.my.app.model.dto.ProductDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FromRequestToDtoOrderProductListConverter implements Converter<UpdateProductListRequest, List<ProductDto>> {
    @Override
    @NonNull
    public List<ProductDto> convert(@NonNull UpdateProductListRequest source) {
        return source.getProducts().stream()
                .map(orderProductRequest -> ProductDto.builder()
                        .id(orderProductRequest.getId())
                        .count(orderProductRequest.getCount())
                        .build())
                .collect(Collectors.toList());
    }
}
