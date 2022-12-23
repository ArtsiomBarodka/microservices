package com.my.app.repository;

import com.my.app.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = {"orders", "orders.orderItems", "orders.orderItems.product"})
    @NonNull
    Optional<User> findById(@NonNull Long id);

    @Override
    @EntityGraph(attributePaths = {"orders", "orders.orderItems", "orders.orderItems.product"})
    @NonNull
    List<User> findAll();
}
