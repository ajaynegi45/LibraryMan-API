package com.libraryman_api.exception;

import java.io.Serial;

/**
 * Custom exception class to handle scenarios where a requested resource
 * is not found in the Library Management System.
 * 
 * <p>This exception is thrown when an operation fails to locate a resource such as
 * a book, member, or borrowing record.</p>
 * 
 * <p>By using this exception, the application can provide meaningful feedback to clients
 * when they attempt to access a resource that does not exist.</p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * The {@code serialVersionUID} is a unique identifier for each version of a serializable class.
     * It is used during the deserialization process to verify that the sender and receiver of a
     * serialized object have loaded classes that are compatible with each other.
     *
     * <p>Including this field is important for ensuring that a serialized class can be successfully
     * deserialized, even if the class definition changes in later versions. If the {@code serialVersionUID}
     * does not match during deserialization, an {@code InvalidClassException} is thrown.</p>
     *
     * <p>This field is optional, but it is good practice to explicitly declare it to prevent
     * automatic generation, which could lead to compatibility issues when the class structure changes.</p>
     *
     * <p>The {@code @Serial} annotation indicates that this field is related to serialization. This
     * annotation is available starting from Java 14 and helps improve clarity regarding the purpose
     * of this field.</p>
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause   the cause of the exception (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
