package com.my.app.model.dto;

import com.epam.app.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Category category;
    private BigDecimal cost;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;

    public ProductDto (String id) {
        this.id = id;
    }
}
