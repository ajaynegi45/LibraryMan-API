package com.libraryman_api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Fines {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "fine_id_generator")
    @SequenceGenerator(name = "fine_id_generator",
            sequenceName = "fine_id_sequence",
            allocationSize = 1)
    @Column(name = "fine_id")
    private int fineId;

/**
 * precision = 10 means the total number of digits (including decimal places) is 10.
 * scale = 2 means the number of decimal places is 2.
 * @Column(nullable = false, precision = 10, scale = 2)
 * */
    @Column(nullable = false, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean paid = false;

    public Fines() {
    }


    public Fines( BigDecimal amount, boolean paid) {

        this.amount = amount;
        this.paid = paid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getFineId() {
        return fineId;
    }
}
