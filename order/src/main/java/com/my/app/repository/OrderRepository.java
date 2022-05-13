package com.my.app.repository;

import com.my.app.model.entity.Order;
import com.my.app.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @NonNull
    Optional<Order> findById(@NonNull Long id);

    @Override
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @NonNull
    List<Order> findAll();
}
