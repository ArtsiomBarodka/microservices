package com.epam.app.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
}
