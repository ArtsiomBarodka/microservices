package com.my.app.command.impl;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.response.CustomerResponse;
import com.my.app.client.CustomerClient;
import com.my.app.command.Operation;
import com.my.app.command.OperationResultStatus;
import com.my.app.model.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerPaymentOperation implements Operation {
    private final CustomerClient customerClient;

    @Override
    @NonNull
    public OperationResultStatus process(@NonNull OrderDto orderDto, @NonNull String token) {
        UpdateCustomerRequest request = toUpdateCustomerRequest(orderDto);
        Optional<CustomerResponse> customerResponse = customerClient.subtractCustomerFund(request, token);
        if (customerResponse.isPresent()) {
            return OperationResultStatus.COMPLETE;
        }
        return OperationResultStatus.FAILED;
    }

    @Override
    @NonNull
    public OperationResultStatus revert(@NonNull OrderDto orderDto, @NonNull String token) {
        UpdateCustomerRequest request = toUpdateCustomerRequest(orderDto);
        Optional<CustomerResponse> customerResponse = customerClient.addCustomerFund(request, token);
        if (customerResponse.isPresent()) {
            return OperationResultStatus.COMPLETE;
        }
        return OperationResultStatus.FAILED;
    }

    private UpdateCustomerRequest toUpdateCustomerRequest(OrderDto orderDto) {
        return UpdateCustomerRequest.builder()
                .id(orderDto.getUserId())
                .fund(orderDto.getTotalCost())
                .build();
    }
}
