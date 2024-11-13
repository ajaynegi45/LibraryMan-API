package com.libraryman_api.fine;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents a fine in the Library Management System.
 * Each fine has an amount, a payment status, and a unique identifier.
 */
@Entity
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fine_id_generator")
    @SequenceGenerator(name = "fine_id_generator", sequenceName = "fine_id_sequence", allocationSize = 1)
    @Column(name = "fine_id", updatable = false, nullable = false)
    private int fineId;

    /**
     * The amount of the fine with a precision of 10 and a scale of 2.
     * Precision = 10 means the total number of digits (including decimal places) is 10.
     * Scale = 2 means the number of decimal places is 2.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Indicates whether the fine has been paid.
     */
    @Column(nullable = false)
    private boolean paid = false;

    // Default constructor for JPA
    public Fine() {
    }

    // Constructor with fields
    public Fine(BigDecimal amount, boolean paid) {
        this.amount = amount;
        this.paid = paid;
    }

    /**
     * Gets the unique ID of the fine.
     *
     * @return the unique fine ID
     */
    public int getFineId() {
        return fineId;
    }

    /**
     * Gets the amount of the fine.
     *
     * @return the amount of the fine
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the fine.
     *
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Checks if the fine has been paid.
     *
     * @return true if the fine is paid, false otherwise
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Sets the payment status of the fine.
     *
     * @param paid true if the fine is paid, false otherwise
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Provides a string representation of the Fine object.
     *
     * @return a string containing the fine details
     */
    @Override
    public String toString() {
        return "Fine{" +
                "fineId=" + fineId +
                ", amount=" + amount +
                ", paid=" + paid +
                '}';
    }
}
