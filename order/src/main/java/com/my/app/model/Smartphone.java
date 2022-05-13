package com.my.app.model;

public class Smartphone extends Product {
    private String storage;

    @Override
    public Category getCategory() {
        return Category.SMARTPHONE;
    }
}
