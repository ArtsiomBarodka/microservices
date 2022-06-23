package com.my.app.model.converter;

import com.epam.app.model.Category;
import com.epam.app.model.Laptop;
import com.epam.app.model.Product;
import com.epam.app.model.Smartphone;
import com.my.app.model.dto.LaptopDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.model.dto.SmartphoneDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FromResponseToDtoProductConverter implements Converter<Product, ProductDto> {
    @Override
    @NonNull
    public ProductDto convert(@NonNull Product source) {
        if (source.getCategory() == Category.LAPTOP && source instanceof Laptop) {
            return FromResponseToDtoProductConverter.laptopConverter.convert((Laptop) source);
        } else if (source.getCategory() == Category.SMARTPHONE && source instanceof Smartphone) {
            return FromResponseToDtoProductConverter.smartphoneConverter.convert((Smartphone) source);
        } else {
            throw new RuntimeException("Unsupported format");
        }
    }

    private static final Converter<Smartphone, SmartphoneDto> smartphoneConverter = source -> {
        return SmartphoneDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .cost(source.getCost())
                .storage(source.getStorage())
                .hasBluetooth(source.getHasBluetooth())
                .build();
    };

    private static final Converter<Laptop, LaptopDto> laptopConverter = source -> {
        return LaptopDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .cost(source.getCost())
                .storage(source.getStorage())
                .ram(source.getRam())
                .processor(source.getProcessor())
                .build();
    };
}
