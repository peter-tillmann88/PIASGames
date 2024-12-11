package com.eecs4413final.demo.dto;

import com.eecs4413final.demo.model.ShoppingCartItems;
import com.eecs4413final.demo.model.User;
import java.sql.Timestamp;
import java.util.Set;

public class ShoppingCartResponseDTO {

    private Long cartId;
    private Long customerId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Set<ShoppingCartItems> shoppingCartItems;
    private User user;
    private String message;

    // Constructors
    public ShoppingCartResponseDTO() {
    }

    public ShoppingCartResponseDTO(Long cartId, Long customerId, Timestamp createdAt, Timestamp updatedAt, Set<ShoppingCartItems> shoppingCartItems) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.shoppingCartItems = shoppingCartItems;
//        this.user = user;
        this.message = message;
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ShoppingCartItems> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(Set<ShoppingCartItems> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
