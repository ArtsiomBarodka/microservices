package com.my.app.model.converter;

import com.epam.app.model.request.UpdateOrderStatusRequest;
import com.my.app.model.dto.OrderStatusDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FromUpdateRequestToDtoOrderStatusConverter implements Converter<UpdateOrderStatusRequest, OrderStatusDto> {
    @Override
    @NonNull
    public OrderStatusDto convert(@NonNull UpdateOrderStatusRequest source) {
        return new OrderStatusDto(source.getId(), null, source.getOrderStatus());
    }
}
