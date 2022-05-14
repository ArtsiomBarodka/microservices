package com.my.app.service.impl;

import com.my.app.model.entity.Order;
import com.my.app.repository.OrderRepository;
import com.my.app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Order getOrderById(@NonNull Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order is not founded"));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
