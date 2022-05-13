package com.my.app.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
}
