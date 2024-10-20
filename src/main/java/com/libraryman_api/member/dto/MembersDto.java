package com.libraryman_api.member.dto;

import java.util.Date;

import com.libraryman_api.member.Role;

public class MembersDto {

    private int memberId;

    private String name;
    
    private String username;

    private String email;

    private String password;


    private Role role;


    private Date membershipDate;

    public MembersDto(int memberId, String name, String username, String email, String password, Role role, Date membershipDate) {
        this.memberId = memberId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.membershipDate = membershipDate;
    }

    public MembersDto() {
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }
    
    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(Date membershipDate) {
        this.membershipDate = membershipDate;
    }

    @Override
    public String toString() {
        return "MembersDto{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", membershipDate=" + membershipDate +
                '}';
    }
}
