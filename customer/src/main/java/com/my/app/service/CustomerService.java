package com.my.app.service;

import com.my.app.model.UpdateOption;
import com.my.app.model.dto.CustomerDto;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface CustomerService {
    @NonNull
    CustomerDto getCustomerById(@NonNull Long id);

    @NonNull
    Collection<CustomerDto> getAllCustomers();


    @NonNull
    CustomerDto updateCustomer(@NonNull CustomerDto customerDto, @NonNull UpdateOption updateOption);
}
