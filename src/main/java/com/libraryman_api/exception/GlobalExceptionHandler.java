package com.libraryman_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.*;

/**
 * Global exception handler for the LibraryMan API. This class provides
 * centralized exception handling across all controllers in the application. It
 * handles specific exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} exceptions. This method is
     * triggered when a {@code ResourceNotFoundException} is thrown in the
     * application. It constructs an {@link ErrorDetails} object containing the
     * exception details and returns a {@link ResponseEntity} with an HTTP status of
     * {@code 404 Not Found}.
     *
     * @param ex      the exception that was thrown.
     * @param request the current web request in which the exception was thrown.
     * @return a {@link ResponseEntity} containing the {@link ErrorDetails} and an
     * HTTP status of {@code 404 Not Found}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link InvalidSortFieldException} exceptions. This method is
     * triggered when an {@code InvalidSortFieldException} is thrown in the
     * application. It constructs an {@link ErrorDetails} object containing the
     * exception details and returns a {@link ResponseEntity} with an HTTP status of
     * {@code 400 Bad Request}.
     *
     * @param ex      the exception that was thrown.
     * @param request the current web request in which the exception was thrown.
     * @return a {@link ResponseEntity} containing the {@link ErrorDetails} and an
     * HTTP status of {@code 400 Bad Request}.
     */
    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<?> invalidSortFieldException(InvalidSortFieldException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link InvalidPasswordException} exceptions. This method is triggered
     * when an {@code InvalidPasswordException} is thrown in the application. It
     * constructs an {@link ErrorDetails} object containing the exception details
     * and returns a {@link ResponseEntity} with an HTTP status of
     * {@code 400 Bad Request}.
     *
     * @param ex      the exception that was thrown.
     * @param request the current web request in which the exception was thrown.
     * @return a {@link ResponseEntity} containing the {@link ErrorDetails} and an
     * HTTP status of {@code 400 Bad Request}.
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> invalidPasswordException(InvalidPasswordException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        HashMap<String,Object> map = new HashMap<>();
        allErrors.forEach(objectError -> {
            String message=objectError.getDefaultMessage();
            String field=((FieldError) objectError).getField();
            map.put(field,message);
        });

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

}
