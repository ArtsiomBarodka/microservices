package com.epam.app.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProductListRequest {
    @NotEmpty
    private List<@NotBlank String> ids;
}
