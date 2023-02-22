package com.my.app.model.dto;

import com.epam.app.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;

    private LocalDateTime created;

    private OrderStatus status;

    private BigDecimal totalCost;

    private UserDto user;

    private List<OrderItemDto> orderItems;
}
