package com.my.app.model.dto;

import com.epam.app.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;
}
