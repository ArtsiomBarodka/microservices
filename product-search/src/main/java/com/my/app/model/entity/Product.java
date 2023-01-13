package com.my.app.model.entity;

import com.epam.app.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Document(indexName = "products-store")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Double, name = "cost")
    private BigDecimal cost;

    @Field(type = FieldType.Keyword, name = "category")
    private Category category;

    @Field(type = FieldType.Integer, name = "count")
    private Integer count;

    @Field(type = FieldType.Date, name = "created", format = DateFormat.basic_date_time)
    private Instant created;

    @Field(type = FieldType.Date, name = "updated", format = DateFormat.basic_date_time)
    private Instant updated;

    @Field(type = FieldType.Keyword, name = "storage")
    private String storage;

    @Field(type = FieldType.Keyword, name = "ram")
    private String ram;

    @Field(type = FieldType.Keyword, name = "processor")
    private String processor;

    @Field(type = FieldType.Keyword, name = "hasBluetooth")
    private String hasBluetooth;
}
