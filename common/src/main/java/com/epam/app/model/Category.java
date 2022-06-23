package com.epam.app.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Category {
    SMARTPHONE,
    LAPTOP;

    private static Map<String, Category> categories = Arrays
            .stream(Category.values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static Category of(String name) {
        return categories.get(name);
    }
}
