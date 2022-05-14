package com.my.app.service;

import com.my.app.model.dto.UserDto;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface UserService {
    @NonNull
    UserDto getUserById(@NonNull Long id);

    @NonNull
    Collection<UserDto> getAllUsers();
}
