package com.eecs4413final.demo.dto;

public class CartIdDTO {
    private Long cartId;
    private String message;

    // Constructors
    public CartIdDTO() {}

    public CartIdDTO(Long cartId) {
        this.cartId = cartId;
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
