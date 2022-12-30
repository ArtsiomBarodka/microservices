package com.my.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long userId;
    private String userEmail;
    private BigDecimal totalCost;
    private Set<OrderItemDto> orderItems;
}
