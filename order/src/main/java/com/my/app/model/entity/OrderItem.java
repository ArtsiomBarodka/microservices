package com.my.app.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_product_id", nullable = false)
    private ProductId product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order_id", nullable = false)
    private Order order;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "count", nullable = false)
    private Integer count;
}
