package com.my.app.repository;

import com.my.app.model.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = "orderItems")
    @NonNull
    Optional<Order> findById(@NonNull Long id);

    @EntityGraph(attributePaths = "orderItems")
    @NonNull
    List<Order> findByUserId(@NonNull Long userId);
}
