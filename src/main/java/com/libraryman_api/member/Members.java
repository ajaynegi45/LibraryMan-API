package com.libraryman_api.member;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "member_id_generator")
    @SequenceGenerator(name = "member_id_generator",
            sequenceName = "member_id_sequence",
            allocationSize = 1)
    @Column(name = "member_id")
    private int memberId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "membership_date")
    private Date membershipDate;


}
