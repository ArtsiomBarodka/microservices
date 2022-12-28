package com.my.app.model.converter;

import com.my.app.model.dto.CustomerDto;
import com.my.app.model.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FromEntityToDtoCustomerConverter implements Converter<Customer, CustomerDto> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public CustomerDto convert(@NonNull Customer source) {
        return modelMapper.map(source, CustomerDto.class);
    }
}
