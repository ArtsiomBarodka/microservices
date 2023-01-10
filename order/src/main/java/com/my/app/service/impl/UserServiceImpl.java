package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.response.CustomerResponse;
import com.my.app.client.CustomerClient;
import com.my.app.model.converter.FromCustomerResponseToUserDtoConverter;
import com.my.app.model.dto.UserDto;
import com.my.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CustomerClient customerClient;
    private final FromCustomerResponseToUserDtoConverter toDtoUserConverter;

    @Override
    @NonNull
    public UserDto getUserById(@NonNull Long userId) {
        log.info("Fetching user with (id = {})", userId);

        final CustomerResponse customerResponse = customerClient.getCustomerById(userId).orElseThrow(() -> {
            log.warn("User with (id = {}) is not found", userId);
            throw new ObjectNotFoundException(String.format("User with (id = %d) is not found", userId));
        });

        log.info("User with (id = {}) is fetched. User: {}", userId, customerResponse);

        return toDtoUserConverter.convert(customerResponse);
    }
}
