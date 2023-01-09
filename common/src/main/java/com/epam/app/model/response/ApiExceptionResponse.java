package com.epam.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiExceptionResponse {
    private HttpStatus statusCode;
    private String message;
    private String caused;
    private int errorCode;
}
