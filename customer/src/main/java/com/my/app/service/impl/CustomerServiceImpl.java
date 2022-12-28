package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.exception.ProcessException;
import com.my.app.model.UpdateOption;
import com.my.app.model.converter.FromEntityToDtoCustomerConverter;
import com.my.app.model.dto.CustomerDto;
import com.my.app.model.entity.Customer;
import com.my.app.repository.CustomerRepository;
import com.my.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final FromEntityToDtoCustomerConverter fromEntityToDtoCustomerConverter;

    @Override
    @NonNull
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CustomerDto getCustomerById(@NonNull Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Customer is not found"));

        return fromEntityToDtoCustomerConverter.convert(customer);
    }

    @Override
    @NonNull
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Collection<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(fromEntityToDtoCustomerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional
    public CustomerDto updateCustomer(@NonNull CustomerDto customerDto, @NonNull UpdateOption updateOption) {
        Long id = customerDto.getId();
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Customer is not found"));

        switch (updateOption) {
            case ADD_FUND:
                existingCustomer.setFund(existingCustomer.getFund().add(customerDto.getFund()));
                break;

            case SUBTRACT_FUND:
                BigDecimal updatedFund = existingCustomer.getFund().subtract(customerDto.getFund());
                if (updatedFund.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ProcessException("");
                }
                existingCustomer.setFund(updatedFund);
                break;

            case UPDATE_INFO:
                populateCustomerFields(existingCustomer, customerDto);
                break;
        }

        return fromEntityToDtoCustomerConverter.convert(existingCustomer);
    }

    private void populateCustomerFields(Customer customer, CustomerDto updatingFields) {
        if (updatingFields.getName() != null) {
            customer.setName(updatingFields.getName());
        }
        if (updatingFields.getEmail() != null) {
            customer.setEmail(updatingFields.getEmail());
        }
    }
}
