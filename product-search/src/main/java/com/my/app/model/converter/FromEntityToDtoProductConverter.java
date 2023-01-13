package com.my.app.model.converter;

import com.my.app.model.dto.ProductDto;
import com.my.app.model.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class FromEntityToDtoProductConverter implements Converter<Product, ProductDto> {
    @Override
    @NonNull
    public ProductDto convert(@NonNull Product source) {
        return ProductDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .cost(source.getCost())
                .category(source.getCategory())
                .count(source.getCount())
                .created(LocalDateTime.ofInstant(source.getCreated(), ZoneOffset.UTC))
                .updated(LocalDateTime.ofInstant(source.getUpdated(), ZoneOffset.UTC))
                .storage(source.getStorage() != null && !source.getStorage().isBlank() ? source.getStorage() : null)
                .ram(source.getRam() != null && !source.getRam().isBlank() ? source.getRam() : null)
                .processor(source.getProcessor() != null && !source.getProcessor().isBlank() ? source.getProcessor() : null)
                .hasBluetooth(source.getHasBluetooth() != null && !source.getHasBluetooth().isBlank() ? Boolean.valueOf(source.getHasBluetooth()) : null)
                .build();

    }
}
