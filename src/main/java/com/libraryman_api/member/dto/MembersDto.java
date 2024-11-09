package com.libraryman_api.member.dto;

import com.libraryman_api.member.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class MembersDto {

    private int memberId;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Please enter a valid email address (e.g., user@example.com)")
    @NotBlank(message = "Email field cannot be empty. Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one letter, one number, and one special character")
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

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
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
