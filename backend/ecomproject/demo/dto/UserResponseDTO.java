package com.eecs4413final.demo.dto;

import com.eecs4413final.demo.model.User;

import java.time.LocalDateTime;

public class UserResponseDTO {

    private Long userID;
    private String username;
    private String email;
    private String phone;
    private User.Role role;
    private LocalDateTime createdAt;

    // Constructor

    public UserResponseDTO(Long userID, String username, String email, String phone, User.Role role, LocalDateTime createdAt) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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

    // Email should be properly validated
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    // Optional field; validate if present
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Automatically set on creation
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}