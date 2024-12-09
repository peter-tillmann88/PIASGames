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
    private String creditCard;
    private String expiryDate;
    private String country;
    private String province;
    private String address;
    private String postalCode;

    public UserResponseDTO(Long userID, String username, String email, String phone, User.Role role,
                           LocalDateTime createdAt, String creditCard, String expiryDate, 
                           String country, String province, String address, String postalCode) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.creditCard = creditCard;
        this.expiryDate = expiryDate;
        this.country = country;
        this.province = province;
        this.address = address;
        this.postalCode = postalCode;
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
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
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
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreditCard() {
        return creditCard;
    }
    
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
