package com.my.app.controller;

import com.my.app.model.converter.FromDtoToVewUserConverter;
import com.my.app.model.dto.UserDto;
import com.my.app.model.view.UserView;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    private FromDtoToVewUserConverter userConverter;

    @GetMapping
    public ResponseEntity<Collection<UserView>> getAllUsers() {
        final Collection<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList.stream().map(userConverter::convert).collect(toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserView> getUserById(@PathVariable @NotNull @Min(1) Long id) {
        final UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userConverter.convert(userDto));
    }
}
