package com.epam.app.model.message;

import com.epam.app.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMessageItem {
    private String id;
    private String name;
    private String description;
    private Integer count;
    private BigDecimal cost;
    private Category category;
    private LocalDateTime updated;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;
}
