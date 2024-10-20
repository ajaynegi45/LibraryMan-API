package com.libraryman_api.member.dto;

public class UpdateMembersDto {

    private String name;
    
    private String username;

    private String email;

    public UpdateMembersDto(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public UpdateMembersDto() {
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

    @Override
    public String toString() {
        return "UpdateMembersDto{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
