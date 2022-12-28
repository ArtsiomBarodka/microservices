package com.my.app.model.converter;

import com.epam.app.model.response.ProductResponse;
import com.my.app.model.dto.ProductDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FromDtoToResponseProductConverter implements Converter<ProductDto, ProductResponse> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public ProductResponse convert(@NonNull ProductDto source) {
        return modelMapper.map(source, ProductResponse.class);
    }
}
