package com.my.app.service.impl;

import com.my.app.model.entity.User;
import com.my.app.repository.UserRepository;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public User getUserById(@NonNull Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User is not founded"));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }
}
