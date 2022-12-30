package com.epam.app.model.response;

import com.epam.app.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponse {
    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;
}
