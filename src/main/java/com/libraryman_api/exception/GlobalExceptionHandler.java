package com.libraryman_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Global exception handler for the LibraryMan API.
 * 
 * <p>This class provides centralized exception handling for all controllers in the application.
 * It intercepts specific exceptions thrown during the execution of the application and returns
 * appropriate HTTP responses to the client.</p>
 *
 * <p>The primary purpose of this class is to ensure that the API responds with meaningful error
 * messages when exceptions occur, thereby improving the client experience.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} exceptions.
     * 
     * <p>This method is triggered when a {@code ResourceNotFoundException} is thrown in the application.
     * It constructs an {@link ErrorDetails} object containing the details of the exception and returns
     * a {@link ResponseEntity} with an HTTP status of {@code 404 Not Found}.</p>
     *
     * <p>By handling this exception globally, the API ensures that all requests resulting in
     * a resource not being found will return a consistent and informative response.</p>
     *
     * @param ex the {@code ResourceNotFoundException} that was thrown.
     * @param request the current web request during which the exception occurred.
     * @return a {@link ResponseEntity} containing the {@link ErrorDetails} and an HTTP status of {@code 404 Not Found}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        // Create an ErrorDetails object to hold error information
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        
        // Return a response entity with the error details and HTTP status
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
