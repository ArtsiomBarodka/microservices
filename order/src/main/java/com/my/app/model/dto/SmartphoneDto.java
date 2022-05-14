package com.my.app.model.dto;

import com.epam.app.model.Category;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmartphoneDto extends ProductDto {
    private String storage;
    private Boolean hasBluetooth;

    @Override
    public Category getCategory() {
        return Category.SMARTPHONE;
    }
}
