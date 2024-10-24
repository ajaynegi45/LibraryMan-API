package com.libraryman_api.exception;

import java.util.Date;

/**
 * The compiler automatically generates the following members:
 * <ul>
 *     <li>A private final field for each component of the record.</li>
 *     <li>A public constructor with parameters for each component.</li>
 *     <li>Public getter methods for each component (the method name is the same as the component name).</li>
 *     <li>A toString() method that includes the names and values of the components.</li>
 *     <li>An equals() method that checks for equality based on the components.</li>
 *     <li>A hashCode() method that calculates a hash code based on the components.</li>
 * </ul>
 * <p>
 * For the ErrorDetails record, the compiler will automatically generate the following members:
 * <p>
 *     <b>Private final fields:</b>
 *     <ul>
 *         <li>private final Date timestamp;</li>
 *         <li>private final String message;</li>
 *         <li>private final String details;</li>
 *     </ul>
 * </p>
 * <p>
 *     <b>Public constructor:</b>
 *     <pre>public ErrorDetails(Date timestamp, String message, String details) { ... }</pre>
 * </p>
 * <p>
 *     <b>Public getter methods:</b>
 *     <ul>
 *         <li>public Date timestamp() { return timestamp; }</li>
 *         <li>public String message() { return message; }</li>
 *         <li>public String details() { return details; }</li>
 *     </ul>
 * </p>
 * <p>
 *     <b>toString() method:</b>
 *     <pre>public String toString() { return "ErrorDetails[timestamp=" + timestamp + ", message=" + message + ", details=" + details + "]"; }</pre>
 * </p>
 * <p>
 *     <b>equals() method:</b>
 *     <pre>@Override
 *     public boolean equals(Object obj) {
 *         if (this == obj) return true;
 *         if (obj == null || getClass() != obj.getClass()) return false;
 *         ErrorDetails other = (ErrorDetails) obj;
 *         return timestamp.equals(other.timestamp) && message.equals(other.message) && details.equals(other.details);
 *     }</pre>
 * </p>
 * <p>
 *     <b>hashCode() method:</b>
 *     <pre>@Override
 *     public int hashCode() {
 *         return java.util.Objects.hash(timestamp, message, details);
 *     }</pre>
 * </p>
 */
public record ErrorDetails(Date timestamp, String message, String details) {
}