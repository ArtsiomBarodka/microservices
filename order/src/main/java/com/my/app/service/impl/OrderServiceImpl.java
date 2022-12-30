package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.OrderStatus;
import com.my.app.model.converter.FromDtoToEntityOrderConverter;
import com.my.app.model.converter.FromEntityToDtoOrderConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.entity.Order;
import com.my.app.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final FromEntityToDtoOrderConverter toDtoOrderConverter;
    private final FromDtoToEntityOrderConverter toEntityOrderConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        final Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order with (id = {}) is not found", id);
                    return new ObjectNotFoundException(String.format("Order with (id = %d) is not found", id));
                });

        log.info("Order with (id = {}) is fetched. Order: {}", id, orderEntity);

        return toDtoOrderConverter.convert(orderEntity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<OrderDto> getAllOrdersByUserId(@NonNull Long id) {
        final List<Order> orderEntities = orderRepository.findByUserId(id);
        if (orderEntities.isEmpty()) {
            log.warn("Orders with (userId = {}) are not found", id);
            throw new ObjectNotFoundException(String.format("Orders with (userId = %d) are not found", id));
        }

        log.info("Orders with (userId = {}) are fetched. Orders: {}", id, orderEntities);

        return orderEntities.stream()
                .map(toDtoOrderConverter::convert)
                .collect(toList());
    }

    @Override
    @Transactional
    @NonNull
    public OrderDto createOrder(@NonNull OrderDto order) {
        final Order orderEntity = toEntityOrderConverter.convert(order);
        orderEntity.setStatus(OrderStatus.CREATED);

        final Order savedOrderEntity = orderRepository.save(orderEntity);

        log.info("The new Order is created. Created order: {}", savedOrderEntity);

        return toDtoOrderConverter.convert(savedOrderEntity);
    }

    @Override
    @Transactional
    @NonNull
    public OrderDto updateOrder(@NonNull OrderDto order) {
        final Order existingOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> {
                    log.warn("Order with (id = {}) is not found", order.getId());
                    return new ObjectNotFoundException(String.format("Order with (id = %d) is not found", order.getId()));
                });

        updateOrderFields(existingOrder, order);
        log.info("Order with (id = {}) is updated. Updated order: {}", order.getId(), existingOrder);

        return toDtoOrderConverter.convert(existingOrder);
    }

    private void updateOrderFields(Order existingOrderEntity, OrderDto newOrderFields) {
        if (newOrderFields.getStatus() != null) {
            existingOrderEntity.setStatus(newOrderFields.getStatus());
        }
    }
}
