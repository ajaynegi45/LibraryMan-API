package com.libraryman_api.member;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MembersDto {

    private int memberId;

    private String name;

    private String email;

    private String password;

    private Role role;

    private Date membershipDate;



}
