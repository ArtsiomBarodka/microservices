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
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final FromDtoToResponseCustomerConverter fromDtoToResponseCustomerConverter;
    private final FromRequestToDtoCustomerConverter fromRequestToDtoCustomerConverter;

    @JsonView(View.Public.class)
    @GetMapping
    public ResponseEntity<Collection<CustomerResponse>> getAllCustomers() {
        final Collection<CustomerDto> customers = customerService.getAllCustomers();

        return ResponseEntity.ok(customers.stream()
                .map(fromDtoToResponseCustomerConverter::convert)
                .collect(Collectors.toList()));
    }

    @JsonView(View.Public.class)
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable @NotNull @Min(1) Long id) {
        final CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(fromDtoToResponseCustomerConverter.convert(customer));
    }

    @JsonView(View.Private.class)
    @GetMapping("/current")
    public ResponseEntity<CustomerResponse> getCurrentCustomer() {
        final CustomerDto customer = customerService.getCustomerById(1L);
        return ResponseEntity.ok(fromDtoToResponseCustomerConverter.convert(customer));
    }

    @JsonView(View.Public.class)
    @PatchMapping("/funds/subtract")
    public ResponseEntity<CustomerResponse> subtractCustomerFund(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        CustomerDto updateFields = fromRequestToDtoCustomerConverter.convert(updateCustomerRequest);
        CustomerDto updatedCustomer = customerService.updateCustomer(updateFields, UpdateOption.SUBTRACT_FUND);

        return ResponseEntity.ok(fromDtoToResponseCustomerConverter.convert(updatedCustomer));
    }

    @JsonView(View.Private.class)
    @PatchMapping("/funds/add")
    public ResponseEntity<CustomerResponse> addCustomerFund(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        CustomerDto updateFields = fromRequestToDtoCustomerConverter.convert(updateCustomerRequest);
        CustomerDto updatedCustomer = customerService.updateCustomer(updateFields, UpdateOption.ADD_FUND);

        return ResponseEntity.ok(fromDtoToResponseCustomerConverter.convert(updatedCustomer));
    }

    @JsonView(View.Private.class)
    @PatchMapping
    public ResponseEntity<CustomerResponse> updateCustomerInfo(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        CustomerDto updateFields = fromRequestToDtoCustomerConverter.convert(updateCustomerRequest);
        CustomerDto updatedCustomer = customerService.updateCustomer(updateFields, UpdateOption.UPDATE_INFO);

        return ResponseEntity.ok(fromDtoToResponseCustomerConverter.convert(updatedCustomer));
    }
}
