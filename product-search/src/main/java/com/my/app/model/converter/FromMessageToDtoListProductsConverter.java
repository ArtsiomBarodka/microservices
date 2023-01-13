package com.my.app.model.converter;

import com.epam.app.model.message.ProductsMessage;
import com.my.app.model.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FromMessageToDtoListProductsConverter implements Converter<ProductsMessage, List<ProductDto>> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public List<ProductDto> convert(@NonNull ProductsMessage source) {
        return source.getProducts().stream()
                .map(messageItem -> modelMapper.map(messageItem, ProductDto.class))
                .collect(Collectors.toList());
    }
}
