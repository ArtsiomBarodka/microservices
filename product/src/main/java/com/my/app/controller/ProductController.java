package com.my.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Value("${app.service.name}")
    private String name;

    @GetMapping
    public String healthCheck(){
        return "Hello from " + name;
    }
}
