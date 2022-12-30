package com.my.app.controller;

import com.epam.app.model.request.UpdateCustomerRequest;
import com.fasterxml.jackson.annotation.JsonView;
import com.my.app.model.UpdateOption;
import com.my.app.model.View;
import com.my.app.model.converter.FromDtoToResponseCustomerConverter;
import com.my.app.model.converter.FromRequestToDtoCustomerConverter;
import com.my.app.model.dto.CustomerDto;
import com.my.app.model.response.CustomerResponse;
import com.my.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final FromDtoToResponseCustomerConverter toResponseCustomerConverter;
    private final FromRequestToDtoCustomerConverter toDtoCustomerConverter;

    @JsonView(View.Public.class)
    @GetMapping
    public ResponseEntity<Collection<CustomerResponse>> getAllCustomers() {
        final Collection<CustomerDto> result = customerService.getAllCustomers();

        final List<CustomerResponse> convertedResult = result.stream()
                .map(toResponseCustomerConverter::convert)
                .collect(Collectors.toList());
        log.info("All Customers response. Customers: {}", convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @JsonView(View.Public.class)
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable @NotNull @Min(1) Long id) {
        final CustomerDto result = customerService.getCustomerById(id);

        final CustomerResponse convertedResult = toResponseCustomerConverter.convert(result);
        log.info("Customer response for (id = {}). Customer: {}", id, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @JsonView(View.Private.class)
    @GetMapping("/current")
    public ResponseEntity<CustomerResponse> getCurrentCustomer() {
        final CustomerDto result = customerService.getCustomerById(1L);

        final CustomerResponse convertedResult = toResponseCustomerConverter.convert(result);
        log.info("Customer response for (id = {}). Customer: {}", 1L, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @JsonView(View.Public.class)
    @PatchMapping("/funds/subtract")
    public ResponseEntity<CustomerResponse> subtractCustomerFund(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        final CustomerDto updateFields = toDtoCustomerConverter.convert(updateCustomerRequest);
        final CustomerDto updatedCustomer = customerService.updateCustomer(updateFields, UpdateOption.SUBTRACT_FUND);

        final CustomerResponse convertedResult = toResponseCustomerConverter.convert(updatedCustomer);
        log.info("Updated Customer response after subtracting funds for (request = {}). Updated Customer: {}", updateCustomerRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @JsonView(View.Private.class)
    @PatchMapping("/funds/add")
    public ResponseEntity<CustomerResponse> addCustomerFund(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        final CustomerDto updateFields = toDtoCustomerConverter.convert(updateCustomerRequest);
        final CustomerDto updatedCustomer = customerService.updateCustomer(updateFields, UpdateOption.ADD_FUND);

        final CustomerResponse convertedResult = toResponseCustomerConverter.convert(updatedCustomer);
        log.info("Updated Customer response after adding funds for (request = {}). Updated Customer: {}", updateCustomerRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }

    @JsonView(View.Private.class)
    @PatchMapping
    public ResponseEntity<CustomerResponse> updateCustomerInfo(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        final CustomerDto updateFields = toDtoCustomerConverter.convert(updateCustomerRequest);
        final CustomerDto updatedCustomer = customerService.updateCustomer(updateFields, UpdateOption.UPDATE_INFO);

        final CustomerResponse convertedResult = toResponseCustomerConverter.convert(updatedCustomer);
        log.info("Updated Customer response for (request = {}). Updated Customer: {}", updateCustomerRequest, convertedResult);

        return ResponseEntity.ok(convertedResult);
    }
}
