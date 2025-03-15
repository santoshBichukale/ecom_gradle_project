package com.zestindiait.globalexceptionhandler;

import com.zestindiait.customeExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            ProductNotFoundException.class,
            CategoryNotFoundException.class,
            OrderNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundExceptions(RuntimeException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getDescription(false));
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            InvalidProductException.class,
            InvalidOrderException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequestExceptions(RuntimeException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getDescription(false));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(InvalidCredentialsException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Validation Error");
        errorDetails.put("path", request.getDescription(false));

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }
        errorDetails.put("validationErrors", validationErrors);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        return buildResponse("Missing request parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST, request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        return buildResponse("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getDescription(false));
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status, String path) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("path", path);
        errorDetails.put("error", status.getReasonPhrase());

        return new ResponseEntity<>(errorDetails, status);
    }
}
