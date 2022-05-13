package com.my.app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_id")
public class ProductId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;
}
