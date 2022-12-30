package com.my.app.command;

import com.my.app.command.impl.CustomerPaymentOperation;
import com.my.app.command.impl.OrderCompleteOperation;
import com.my.app.command.impl.ProductSubtractionOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationsProvider {
    private final CustomerPaymentOperation customerPaymentOperation;
    private final ProductSubtractionOperation productSubtractionOperation;
    private final OrderCompleteOperation orderCompleteOperation;

    private List<Operation> allProcessOperations;
    private List<Operation> requiredFinalRejectedOperations;

    @PostConstruct
    private void init() {
        // the adding operations order is important
        allProcessOperations = new ArrayList<>();
        allProcessOperations.add(productSubtractionOperation);
        allProcessOperations.add(customerPaymentOperation);
        allProcessOperations.add(orderCompleteOperation);

        requiredFinalRejectedOperations = new ArrayList<>();
        requiredFinalRejectedOperations.add(orderCompleteOperation);
    }

    public List<Operation> getAllProcessOperations() {
        return Collections.unmodifiableList(allProcessOperations);
    }

    public List<Operation> getRequiredFinalRejectedOperations() {
        return Collections.unmodifiableList(requiredFinalRejectedOperations);
    }
}
