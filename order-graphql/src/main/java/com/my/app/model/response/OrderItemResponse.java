package com.my.app.model.response;

import com.epam.app.model.response.ProductResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponse {
    private Long id;

    private ProductResponse product;

    private BigDecimal cost;

    private Integer count;
}
