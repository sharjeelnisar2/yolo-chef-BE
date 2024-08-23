package com.yolo.chef.user;

import java.util.List;

public class UserDetails {
    private String username;
    private String email;
    private List<String> roles;
    private String name; // This is sensitive information, ensure it's handled securely

    // Constructors, getters, and setters

    public UserDetails() {}

    public UserDetails(String username, String email, List<String> roles, String name) {
        this.username = username;
        this.email = email;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
