package com.my.app.model.converter;

import com.epam.app.model.response.ProductResponse;
import com.my.app.model.dto.ProductDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FromResponseToDtoProductConverter implements Converter<ProductResponse, ProductDto> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public ProductDto convert(@NonNull ProductResponse source) {
        return modelMapper.map(source, ProductDto.class);
    }
}
