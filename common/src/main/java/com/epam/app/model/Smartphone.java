package com.epam.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Smartphone extends Product {
    private String storage;
    private Boolean hasBluetooth;

    @Override
    public Category getCategory() {
        return Category.SMARTPHONE;
    }
}
