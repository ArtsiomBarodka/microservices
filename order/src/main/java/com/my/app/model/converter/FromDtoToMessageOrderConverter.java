package com.my.app.model.converter;

import com.epam.app.model.message.OrderItemMessage;
import com.epam.app.model.message.OrderMessage;
import com.my.app.model.dto.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.util.stream.Collectors.toSet;

@Service
public class FromDtoToMessageOrderConverter implements Converter<OrderDto, OrderMessage> {
    @Override
    @NonNull
    public OrderMessage convert(@NonNull OrderDto source) {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(source.getId());
        orderMessage.setUserId(source.getUser().getId());
        orderMessage.setUserEmail(source.getUser().getEmail());
        orderMessage.setOrderItems(source.getOrderItems()
                .stream()
                .map(item -> new OrderItemMessage(item.getProduct().getId(), item.getCount()))
                .collect(toSet()));
        orderMessage.setTotalCost(source.getOrderItems().stream()
                .map(orderItem -> BigDecimal.valueOf(orderItem.getCount()).multiply(orderItem.getCost()))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return orderMessage;
    }
}

