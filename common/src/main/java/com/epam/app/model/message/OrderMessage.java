package com.epam.app.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Long orderId;
    private Long userId;
    private String userEmail;
    private BigDecimal totalCost;
    private Set<OrderItemMessage> orderItems;
}
