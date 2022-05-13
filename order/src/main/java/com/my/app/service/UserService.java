package com.my.app.service;

import com.my.app.model.entity.User;
import org.springframework.lang.NonNull;

public interface UserService {
    @NonNull User getUserById(@NonNull Long id);
}
