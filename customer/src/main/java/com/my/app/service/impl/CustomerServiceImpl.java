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
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final FromEntityToDtoCustomerConverter toDtoCustomerConverter;

    @Override
    @NonNull
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CustomerDto getCustomerById(@NonNull Long id) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with (id = {}) is not found", id);
                    return new ObjectNotFoundException(String.format("Customer with (id = %d) is not found", id));
                });
        log.info("Customer with (id = {}) is fetched. Customer: {}", id, customer);

        return toDtoCustomerConverter.convert(customer);
    }

    @Override
    @NonNull
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Collection<CustomerDto> getAllCustomers() {
        final List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            log.warn("Customers are not found");
            throw new ObjectNotFoundException("Customers are not found");
        }

        log.info("All Customers are fetched. Customers: {}", customers);

        return customers.stream()
                .map(toDtoCustomerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional
    public CustomerDto updateCustomer(@NonNull CustomerDto customerDto, @NonNull UpdateOption updateOption) {
        final Long id = customerDto.getId();
        final Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with (id = {}) is not found", id);
                    return new ObjectNotFoundException(String.format("Customer with (id = %d) is not found", id));
                });

        switch (updateOption) {
            case ADD_FUND:
                existingCustomer.setFund(existingCustomer.getFund().add(customerDto.getFund()));
                break;

            case SUBTRACT_FUND:
                final BigDecimal updatedFund = existingCustomer.getFund().subtract(customerDto.getFund());
                if (updatedFund.compareTo(BigDecimal.ZERO) < 0) {
                    log.warn("Customer funds after operation can't be less than 0. New funds = {}, Customer id = {} Customer funds = {}",
                            updatedFund, existingCustomer.getId(), existingCustomer.getFund());

                    throw new ProcessException(String.format(
                            "Customer funds after operation can't be less than 0. New funds = %s, Customer id = %d Customer funds = %s",
                            updatedFund, existingCustomer.getId(), existingCustomer.getFund()));
                }
                existingCustomer.setFund(updatedFund);
                break;

            case UPDATE_INFO:
                populateCustomerFields(existingCustomer, customerDto);
                break;
        }

        log.info("Customer with (id = {}) is updated. Updated Customer: {}", id, existingCustomer);

        return toDtoCustomerConverter.convert(existingCustomer);
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
