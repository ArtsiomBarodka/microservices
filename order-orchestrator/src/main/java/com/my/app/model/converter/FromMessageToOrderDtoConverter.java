package com.my.app.model.converter;

import com.epam.app.model.message.OrderMessage;
import com.my.app.model.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FromMessageToOrderDtoConverter implements Converter<OrderMessage, OrderDto> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public OrderDto convert(@NonNull OrderMessage source) {
        return modelMapper.map(source, OrderDto.class);
    }
}
