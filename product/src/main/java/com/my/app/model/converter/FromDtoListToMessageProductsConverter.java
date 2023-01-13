package com.my.app.model.converter;

import com.epam.app.model.message.ProductMessageItem;
import com.epam.app.model.message.ProductsMessage;
import com.my.app.model.dto.ProductDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FromDtoListToMessageProductsConverter implements Converter<Collection<ProductDto>, ProductsMessage> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public ProductsMessage convert(@NonNull Collection<ProductDto> source) {
        final Set<ProductMessageItem> productMessageItems = source.stream()
                .map(productDto -> modelMapper.map(productDto, ProductMessageItem.class))
                .collect(Collectors.toSet());

        return new ProductsMessage(productMessageItems);
    }
}