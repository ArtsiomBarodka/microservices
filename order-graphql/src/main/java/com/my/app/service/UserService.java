package com.my.app.service;

import com.my.app.model.dto.UserDto;
import org.springframework.lang.NonNull;

public interface UserService {
    @NonNull UserDto getUserById (@NonNull Long userId);
}
