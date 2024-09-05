package com.libraryman_api.controller;

import com.libraryman_api.exception.ErrorDetails;
import com.libraryman_api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Global exception handler for the LibraryMan API.
 * This class provides centralized exception handling across all controllers in the application.
 * It handles specific exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} exceptions.
     * This method is triggered when a {@code ResourceNotFoundException} is thrown in the application.
     * It constructs an {@link ErrorDetails} object containing the exception details and returns
     * a {@link ResponseEntity} with an HTTP status of {@code 404 Not Found}.
     *
     * @param ex the exception that was thrown.
     * @param request the current web request in which the exception was thrown.
     * @return a {@link ResponseEntity} containing the {@link ErrorDetails} and an HTTP status of {@code 404 Not Found}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
