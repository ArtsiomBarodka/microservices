package com.epam.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
    private LocalDateTime created;
    private LocalDateTime updated;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;
}
