package com.my.app.controller;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.exception.ProcessException;
import com.epam.app.model.response.ApiExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.epam.app.constants.MessageExceptions.ACCESS_DENIED_EXCEPTION;
import static com.epam.app.constants.MessageExceptions.AUTHENTICATION_EXCEPTION;
import static com.epam.app.constants.MessageExceptions.HANDLE_PROCESS_EXCEPTION;
import static com.epam.app.constants.MessageExceptions.OBJECT_NOT_FOUND_EXCEPTION;
import static com.epam.app.constants.MessageExceptions.OTHER_ERRORS_EXCEPTION;
import static com.epam.app.constants.MessageExceptions.VALIDATION_EXCEPTION;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ObjectNotFoundException ex) {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.NOT_FOUND, OBJECT_NOT_FOUND_EXCEPTION, ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @ExceptionHandler(value = ProcessException.class)
    protected ResponseEntity<Object> handleProcessException(ProcessException ex) {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.BAD_REQUEST, HANDLE_PROCESS_EXCEPTION, ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_EXCEPTION, ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.UNAUTHORIZED, AUTHENTICATION_EXCEPTION, ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.FORBIDDEN, ACCESS_DENIED_EXCEPTION, ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleALLException() {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, OTHER_ERRORS_EXCEPTION, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String message = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(this::buildMessage)
                .collect(Collectors.toList()).toString();

        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_EXCEPTION, message, HttpStatus.BAD_REQUEST.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    private String buildMessage(FieldError fe) {
        return "error." + fe.getObjectName() + "." +
                fe.getField() + "." +
                Objects.requireNonNullElse(fe.getCode(), "").toLowerCase();
    }

    private ResponseEntity<Object> buildResponseEntity(ApiExceptionResponse apiExceptionResponse) {
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.getStatusCode());
    }
}
