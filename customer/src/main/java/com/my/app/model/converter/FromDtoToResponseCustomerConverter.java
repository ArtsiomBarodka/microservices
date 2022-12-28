package com.my.app.model.converter;

import com.my.app.model.dto.CustomerDto;
import com.my.app.model.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FromDtoToResponseCustomerConverter implements Converter<CustomerDto, CustomerResponse> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public CustomerResponse convert(@NonNull CustomerDto source) {
        return modelMapper.map(source, CustomerResponse.class);
    }
}
