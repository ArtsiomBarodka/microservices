package com.epam.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Laptop extends Product{
    private String storage;
    private String ram;
    private String processor;

    @Override
    public Category getCategory() {
        return Category.LAPTOP;
    }
}
