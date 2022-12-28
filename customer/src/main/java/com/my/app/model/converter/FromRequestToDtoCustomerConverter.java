package com.my.app.model.converter;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.my.app.model.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FromRequestToDtoCustomerConverter implements Converter<UpdateCustomerRequest, CustomerDto> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public CustomerDto convert(@NonNull UpdateCustomerRequest source) {
        return modelMapper.map(source, CustomerDto.class);
    }
}
