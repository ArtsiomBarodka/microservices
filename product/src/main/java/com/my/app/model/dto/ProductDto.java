package com.my.app.model.dto;

import com.epam.app.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Integer count;
    private BigDecimal cost;
    private Category category;
    private LocalDateTime created;
    private LocalDateTime updated;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;
}
