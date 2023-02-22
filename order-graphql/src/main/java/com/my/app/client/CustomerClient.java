package com.my.app.client;

import com.epam.app.model.response.CustomerResponse;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerClient {
    @NonNull Optional<CustomerResponse> getCustomerById(@NonNull Long id);
}
