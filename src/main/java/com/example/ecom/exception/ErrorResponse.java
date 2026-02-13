package com.example.ecom.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.time.LocalDateTime;

/**
 * Standard error response model used for API exception handling.
 * Encapsulates HTTP status, error messages, and timestamp details.
 */
@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private List<String> errors;
}
