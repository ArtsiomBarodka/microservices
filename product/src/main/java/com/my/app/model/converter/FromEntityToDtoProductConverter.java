package com.my.app.model.converter;

import com.my.app.model.dto.ProductDto;
import com.my.app.model.entity.Product;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FromEntityToDtoProductConverter implements Converter<Product, ProductDto> {
    private final ModelMapper modelMapper;

    @Override
    public ProductDto convert(@NonNull Product source) {
        if (source == null){
            return null;
        }

        return modelMapper.map(source, ProductDto.class);
    }
}
