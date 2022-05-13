package com.my.app.service;

import com.my.app.model.entity.Order;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface OrderService {
    @NonNull Collection<Order> getAllOrders();
}
