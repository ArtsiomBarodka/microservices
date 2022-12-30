package com.my.app.client;

import com.epam.app.model.response.CustomerResponse;
import org.springframework.lang.NonNull;

public interface CustomerClient {
    CustomerResponse getCustomerById(@NonNull Long id);
}
