package com.my.app.model.converter;

import com.epam.app.model.response.OrderStatusResponse;
import com.my.app.model.dto.OrderStatusDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FromDtoToResponseOrderStatusConverter implements Converter<OrderStatusDto, OrderStatusResponse> {
    private final ModelMapper modelMapper;

    @Override
    @NonNull
    public OrderStatusResponse convert(@NonNull OrderStatusDto source) {
        return modelMapper.map(source, OrderStatusResponse.class);
    }
}
