package com.epam.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
}
