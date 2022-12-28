package com.my.app.client;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.response.CustomerResponse;
import org.springframework.lang.NonNull;

public interface CustomerClient {
    CustomerResponse subtractCustomerFund(@NonNull UpdateCustomerRequest updateCustomerRequest);

    CustomerResponse getCustomerById(@NonNull Long id);
}
