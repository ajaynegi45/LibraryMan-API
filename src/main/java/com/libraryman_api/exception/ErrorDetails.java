package com.libraryman_api.exception;

import java.util.Date;

/**
 * A record representing error details for exceptions in the LibraryMan system.
 * 
 * <p>The {@code ErrorDetails} record captures essential information about errors
 * that occur within the application, such as the time the error occurred, a message 
 * describing the error, and any additional details that may be helpful for debugging.</p>
 * 
 * <p>The compiler automatically generates several members for this record, including:</p>
 * <ul>
 *     <li>A private final field for each component of the record.</li>
 *     <li>A public constructor with parameters for each component.</li>
 *     <li>Public getter methods for each component, with names corresponding to the component names.</li>
 *     <li>A {@code toString()} method that includes the names and values of the components.</li>
 *     <li>An {@code equals()} method that checks for equality based on the components.</li>
 *     <li>A {@code hashCode()} method that calculates a hash code based on the components.</li>
 * </ul>
 *
 * <h3>Record Components:</h3>
 * <p>The following private final fields are automatically generated:</p>
 * <ul>
 *     <li><code>private final Date timestamp;</code> - The date and time when the error occurred.</li>
 *     <li><code>private final String message;</code> - A brief message describing the error.</li>
 *     <li><code>private final String details;</code> - Additional details about the error that may be useful for troubleshooting.</li>
 * </ul>
 *
 * <h3>Public Methods:</h3>
 * <p>The following public methods are automatically generated for the record:</p>
 * <ul>
 *     <li><code>public Date timestamp() { return timestamp; }</code> - Returns the timestamp of the error.</li>
 *     <li><code>public String message() { return message; }</code> - Returns the error message.</li>
 *     <li><code>public String details() { return details; }</code> - Returns any additional error details.</li>
 * </ul>
 *
 * <h3>Automatically Generated Methods:</h3>
 * <p>The following methods are also generated:</p>
 * <ul>
 *     <li><b>toString() method:</b>
 *         <pre>public String toString() { return "ErrorDetails[timestamp=" + timestamp + ", message=" + message + ", details=" + details + "]"; }</pre>
 *     </li>
 *     <li><b>equals() method:</b>
 *         <pre>@Override
 *         public boolean equals(Object obj) {
 *             if (this == obj) return true;
 *             if (obj == null || getClass() != obj.getClass()) return false;
 *             ErrorDetails other = (ErrorDetails) obj;
 *             return timestamp.equals(other.timestamp) && message.equals(other.message) && details.equals(other.details);
 *         }</pre>
 *     </li>
 *     <li><b>hashCode() method:</b>
 *         <pre>@Override
 *         public int hashCode() {
 *             return java.util.Objects.hash(timestamp, message, details);
 *         }</pre>
 *     </li>
 * </ul>
 */
public record ErrorDetails(Date timestamp, String message, String details) {
}
