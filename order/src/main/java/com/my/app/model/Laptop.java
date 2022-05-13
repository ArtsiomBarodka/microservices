package com.my.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Laptop extends Product {
    private String storage;
    private String ram;
    private String processor;

    @Override
    public Category getCategory() {
        return Category.LAPTOP;
    }
}
