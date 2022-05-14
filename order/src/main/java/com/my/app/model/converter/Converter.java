package com.my.app.model.converter;

import org.springframework.lang.NonNull;

@FunctionalInterface
public interface Converter<S, T> {
    @NonNull
    T convert(@NonNull S source);
}
