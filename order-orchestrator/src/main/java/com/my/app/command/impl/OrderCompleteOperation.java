package com.my.app.command.impl;

import com.epam.app.model.OrderStatus;
import com.epam.app.model.request.UpdateOrderStatusRequest;
import com.epam.app.model.response.OrderStatusResponse;
import com.my.app.client.OrderClient;
import com.my.app.command.Operation;
import com.my.app.command.OperationResultStatus;
import com.my.app.model.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCompleteOperation implements Operation {
    private final OrderClient orderClient;

    @Override
    @NonNull
    public OperationResultStatus process(@NonNull OrderDto orderDto, @NonNull String token) {
        final UpdateOrderStatusRequest request = toUpdateOrderStatusRequest(orderDto, OrderStatus.COMPLETED);
        final Optional<OrderStatusResponse> response = orderClient.updateOrderStatus(request, token);
        if (response.isPresent()) {
            return OperationResultStatus.COMPLETE;
        }
        return OperationResultStatus.FAILED;
    }

    @Override
    @NonNull
    public OperationResultStatus revert(@NonNull OrderDto orderDto, @NonNull String token) {
        final UpdateOrderStatusRequest request = toUpdateOrderStatusRequest(orderDto, OrderStatus.REJECTED);
        final Optional<OrderStatusResponse> response = orderClient.updateOrderStatus(request, token);
        if (response.isPresent()) {
            return OperationResultStatus.COMPLETE;
        }
        return OperationResultStatus.FAILED;
    }

    private UpdateOrderStatusRequest toUpdateOrderStatusRequest(OrderDto orderDto, OrderStatus orderStatus) {
        return new UpdateOrderStatusRequest(orderDto.getOrderId(), orderStatus);
    }
}
