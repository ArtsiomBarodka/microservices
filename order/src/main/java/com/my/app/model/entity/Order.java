package com.my.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public void addOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return;
        }

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems.addAll(orderItems);
        orderItems.forEach(orderItem -> orderItem.setOrder(this));
    }
}
