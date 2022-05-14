package com.my.app.model.dto;

import com.epam.app.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
}
