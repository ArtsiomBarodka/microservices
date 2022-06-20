package com.my.app.model;

import com.epam.app.model.Category;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
    @CreatedDate
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime updated;

    private String storage;
    private String ram;
    private String processor;
    private Boolean hasBluetooth;
}
