package com.my.app.model.dto;

import com.epam.app.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {
    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;
}
