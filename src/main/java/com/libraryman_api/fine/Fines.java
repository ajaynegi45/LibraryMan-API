package com.libraryman_api.fine;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}
