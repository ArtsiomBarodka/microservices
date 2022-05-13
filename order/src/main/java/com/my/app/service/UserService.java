package com.my.app.service;

import com.my.app.model.entity.User;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface UserService {
    @NonNull
    User getUserById(@NonNull Long id);

    @NonNull
    Collection<User> getAllUsers();
}
