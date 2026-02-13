package com.example.ecom.exception;

/**
 * Custom exception thrown when a requested resource
 * is not found in the system.
 * <p>
 * Extends RuntimeException to allow unchecked exception handling.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException
     * with the specified error message.
     *
     * @param message detailed description of the exception
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
