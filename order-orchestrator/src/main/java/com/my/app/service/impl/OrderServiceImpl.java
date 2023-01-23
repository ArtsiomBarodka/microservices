package com.my.app.service.impl;

import com.my.app.command.Operation;
import com.my.app.command.OperationResultStatus;
import com.my.app.command.OperationsProvider;
import com.my.app.config.PropertiesConfig;
import com.my.app.model.dto.OrderDto;
import com.my.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OperationsProvider operationsProvider;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void processOrder(@NonNull OrderDto orderDto, @NonNull String token) {
        log.info("Starting operations process...");

        final List<Operation> operations = operationsProvider.getAllProcessOperations();
        final List<Operation> finishedOperations = new ArrayList<>(operations.size());

        log.info("List of operations: {}", operations.stream()
                .map(operation -> operation.getClass().getName())
                .collect(Collectors.toList()));

        for (Operation operation : operations) {
            OperationResultStatus resultStatus = operation.process(orderDto, token);

            if (resultStatus == OperationResultStatus.COMPLETE) {
                log.info("Process Operation {} is completed for Order: {}", operation.getClass().getName(), orderDto);
                finishedOperations.add(operation);
            } else {
                log.warn("Process Operation {} is failed for Order: {}", operation.getClass().getName(), orderDto);
                processRevert(finishedOperations, orderDto, token);
                break;
            }
        }

        log.info("Operations process was finished");
    }

    private void processRevert(List<Operation> finishedOperations, OrderDto orderDto, String token) {
        log.info("Starting revert process...");

        Collections.reverse(finishedOperations);
        finishedOperations.addAll(operationsProvider.getRequiredFinalRejectedOperations());
        final List<Operation> notRevertedOperations = new ArrayList<>(finishedOperations.size());

        log.info("List of reverting operations: {}", finishedOperations.stream()
                .map(operation -> operation.getClass().getName())
                .collect(Collectors.toList()));

        for (Operation operation : finishedOperations) {
            OperationResultStatus resultStatus = operation.revert(orderDto, token);

            if (resultStatus == OperationResultStatus.FAILED) {
                // if revert operation is failed, try again
                for (int i = 0; i < propertiesConfig.getOperationRetriesCount(); i++) {
                    resultStatus = operation.revert(orderDto, token);

                    // if revert operation is completed, return to apply remaining revert operations
                    if (resultStatus == OperationResultStatus.COMPLETE) {
                        log.info("Reject Operation {} is completed for Order: {}", operation.getClass().getName(), orderDto);
                        break;
                    } else {
                        log.info("Reject Operation {} is failed for Order: {}", operation.getClass().getName(), orderDto);
                    }
                }

                // if finally revert operation is still failed, add to notRevertedOperations list
                if (resultStatus == OperationResultStatus.FAILED) {
                    notRevertedOperations.add(operation);
                }
            } else {
                log.info("Reject Operation {} is completed for Order: {}", operation.getClass().getName(), orderDto);
            }
        }

        if (!notRevertedOperations.isEmpty()) {
            log.error("Some revert operations were failed. Operations: {}; Order: {}",
                    notRevertedOperations.stream()
                            .map(operation -> operation.getClass().getName())
                            .collect(Collectors.toList()), orderDto);
        }

        log.info("Reverting process was finished!");
    }
}
