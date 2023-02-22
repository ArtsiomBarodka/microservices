package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.client.OrderClient;
import com.my.app.model.converter.FromResponseToDtoOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.response.OrderResponse;
import com.my.app.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderClient orderClient;
    private final FromResponseToDtoOrderConverter toDtoOrderConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        final OrderResponse orderResponse = orderClient.getOrderById(id)
                .orElseThrow(() -> {
                    log.warn("Order with (id = {}) is not found", id);
                    return new ObjectNotFoundException(String.format("Order with (id = %d) is not found", id));
                });

        log.info("Order with (id = {}) is fetched. Order: {}", id, orderResponse);

        return toDtoOrderConverter.convert(orderResponse);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<OrderDto> getAllOrdersByUserId(@NonNull Long id) {
        final List<OrderResponse> orderResponse = orderClient.getAllOrdersByUserId(id);
        if (orderResponse.isEmpty()) {
            log.warn("Orders with (userId = {}) are not found", id);
            throw new ObjectNotFoundException(String.format("Orders with (userId = %d) are not found", id));
        }

        log.info("Orders with (userId = {}) are fetched. Orders: {}", id, orderResponse);

        return orderResponse.stream()
                .map(toDtoOrderConverter::convert)
                .collect(toList());
    }
}
