package com.my.app.model.dto;

import com.epam.app.model.Category;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LaptopDto extends ProductDto {
    private String storage;
    private String ram;
    private String processor;

    @Override
    public Category getCategory() {
        return Category.LAPTOP;
    }
}
