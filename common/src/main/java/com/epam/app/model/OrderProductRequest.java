package com.epam.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderProductRequest extends ProductRequest {
    @Min(1)
    @NotNull
    private Integer count;
}
