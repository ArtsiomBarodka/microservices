package com.my.app.model.converter;

import com.epam.app.model.response.CustomerResponse;
import com.my.app.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FromCustomerResponseToUserDtoConverter implements Converter<CustomerResponse, UserDto> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public UserDto convert(@NonNull CustomerResponse source) {
        return modelMapper.map(source, UserDto.class);
    }
}
