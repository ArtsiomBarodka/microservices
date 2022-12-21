package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.my.app.model.converter.FromEntityToDtoUserConverter;
import com.my.app.model.dto.UserDto;
import com.my.app.model.entity.User;
import com.my.app.repository.UserRepository;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FromEntityToDtoUserConverter userConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public UserDto getUserById(@NonNull Long id) {
        final User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User is not found"));

        return userConverter.convert(userEntity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<UserDto> getAllUsers() {
        final List<User> userEntities = userRepository.findAll();

        return userEntities.stream()
                .map(userConverter::convert)
                .collect(toList());
    }
}
