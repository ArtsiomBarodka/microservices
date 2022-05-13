package com.my.app.service.impl;

import com.my.app.model.entity.User;
import com.my.app.repository.UserRepository;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User is not founded"));
    }
}
