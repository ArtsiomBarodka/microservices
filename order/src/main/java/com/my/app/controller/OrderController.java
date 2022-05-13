package com.my.app.controller;

import com.my.app.model.entity.User;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@RefreshScope
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private UserService userService;

    @Value("${app.service.name}")
    private String name;

    @GetMapping
    public String healthCheck(){
        return "Hello from " + name;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @NotNull @Min(1) Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
