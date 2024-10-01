package com.libraryman_api.fine;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents a fine in the Library Management System.
 * 
 * <p>This entity is responsible for storing information about fines incurred by library members,
 * including the amount of the fine and whether it has been paid.</p>
 */
@Entity
public class Fines {

    /**
     * Unique identifier for the fine.
     * <p>This field is automatically generated using a sequence generator.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fine_id_generator")
    @SequenceGenerator(name = "fine_id_generator", sequenceName = "fine_id_sequence", allocationSize = 1)
    @Column(name = "fine_id")
    private int fineId;

    /**
     * The amount of the fine.
     * <p>This field has a precision of 10, allowing for a total of 10 digits,
     * with 2 decimal places. It cannot be null.</p>
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Indicates whether the fine has been paid.
     * <p>This field is set to false by default, indicating that the fine is unpaid when created.</p>
     */
    @Column(nullable = false)
    private boolean paid = false;

    /**
     * Default constructor for the Fines entity.
     */
    public Fines() {
    }

    /**
     * Constructs a new Fines object with the specified amount and payment status.
     *
     * @param amount the amount of the fine
     * @param paid   indicates whether the fine has been paid
     */
    public Fines(BigDecimal amount, boolean paid) {
        this.amount = amount;
        this.paid = paid;
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
     * @return true if the fine has been paid, false otherwise
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Sets the payment status of the fine.
     *
     * @param paid indicates whether the fine has been paid
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Gets the unique identifier of the fine.
     *
     * @return the fine ID
     */
    public int getFineId() {
        return fineId;
    }
}
