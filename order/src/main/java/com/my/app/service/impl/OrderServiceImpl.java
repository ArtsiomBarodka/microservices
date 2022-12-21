package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.model.converter.FromDtoToEntityOrderConverter;
import com.my.app.model.converter.FromEntityToDtoOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.entity.Order;
import com.my.app.repository.OrderRepository;
import com.my.app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final FromEntityToDtoOrderConverter toDtoOrderConverter;
    private final FromDtoToEntityOrderConverter toEntityOrderConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        final Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order is not found"));

        return toDtoOrderConverter.convert(orderEntity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<OrderDto> getAllOrders() {
        final List<Order> orderEntities = orderRepository.findAll();

        return orderEntities.stream()
                .map(toDtoOrderConverter::convert)
                .collect(toList());
    }

    @Override
    @Transactional
    @NonNull
    public OrderDto createOrder(@NonNull OrderDto order) {
        final Order orderEntity = toEntityOrderConverter.convert(order);
        final Order savedOrderEntity = orderRepository.save(orderEntity);
        return toDtoOrderConverter.convert(savedOrderEntity);
    }
}
