package com.my.app.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.my.app.model.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    @JsonView(View.Public.class)
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonView(View.Private.class)
    private LocalDateTime created;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonView(View.Private.class)
    private LocalDateTime updated;

    @JsonView(View.Public.class)
    private String name;

    @JsonView(View.Public.class)
    private String email;

    @JsonView(View.Private.class)
    private BigDecimal fund;
}
