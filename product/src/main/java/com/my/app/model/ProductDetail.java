package com.my.app.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetail {
    private String id;
    private String name;
    private String description;
    private BigDecimal cost;
    private String category;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;
}
