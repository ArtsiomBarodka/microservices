package com.my.app.command.impl;

import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.request.UpdateProductRequest;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.command.Operation;
import com.my.app.command.OperationResultStatus;
import com.my.app.model.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductSubtractionOperation implements Operation {
    private final ProductClient productClient;


    @Override
    @NonNull
    public OperationResultStatus process(@NonNull OrderDto orderDto) {
        UpdateProductListRequest request = toUpdateProductListRequest(orderDto);
        List<ProductResponse> productResponses = productClient.subtractProductsCount(request);
        if (productResponses == null || productResponses.isEmpty()) {
            return OperationResultStatus.FAILED;
        }
        return OperationResultStatus.COMPLETE;
    }

    @Override
    @NonNull
    public OperationResultStatus revert(@NonNull OrderDto orderDto) {
        UpdateProductListRequest request = toUpdateProductListRequest(orderDto);
        List<ProductResponse> productResponses = productClient.addProductsCount(request);
        if (productResponses == null || productResponses.isEmpty()) {
            return OperationResultStatus.FAILED;
        }
        return OperationResultStatus.COMPLETE;
    }

    private UpdateProductListRequest toUpdateProductListRequest(OrderDto orderDto) {
        List<UpdateProductRequest> requestItems = orderDto.getOrderItems().stream()
                .map(item -> {
                    UpdateProductRequest updateProductRequest = new UpdateProductRequest();
                    updateProductRequest.setId(item.getProductId());
                    updateProductRequest.setCount(item.getCount());
                    return updateProductRequest;
                })
                .collect(Collectors.toList());

        return new UpdateProductListRequest(requestItems);
    }
}
