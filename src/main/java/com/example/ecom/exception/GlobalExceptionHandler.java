package com.example.ecom.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * <p>
 * Provides centralized exception handling across all controllers
 * using @ControllerAdvice. Converts exceptions into structured
 * ErrorResponse objects with appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation failures triggered by @Valid annotations.
     * <p>
     * Captures field-level validation errors and returns a
     * BAD_REQUEST (400) response with detailed error messages.
     */


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .build();
        log.error("Validation failed :{}", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ResourceNotFoundException.
     * <p>
     * Returns NOT_FOUND (404) status when a requested resource
     * does not exist.
     */
    //ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error("Resource not found:{}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all uncaught exceptions.
     * <p>
     * Returns INTERNAL_SERVER_ERROR (500) for unexpected
     * application failures.
     */
    //Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse reponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errors(List.of(ex.getMessage()))
                .build();
        log.error("unexpected error", ex);
        return new ResponseEntity<>(reponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


