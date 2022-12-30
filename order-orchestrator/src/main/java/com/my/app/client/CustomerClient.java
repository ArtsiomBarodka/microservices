package com.my.app.client;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.response.CustomerResponse;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerClient {
    Optional<CustomerResponse> subtractCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest);

    Optional<CustomerResponse> addCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest);
}
