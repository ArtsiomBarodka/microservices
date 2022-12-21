package com.my.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
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
