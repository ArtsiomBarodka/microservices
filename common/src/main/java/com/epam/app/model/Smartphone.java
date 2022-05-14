package com.epam.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Smartphone extends Product {
    private String storage;
    private Boolean hasBluetooth;

    @Override
    public Category getCategory() {
        return Category.SMARTPHONE;
    }
}
