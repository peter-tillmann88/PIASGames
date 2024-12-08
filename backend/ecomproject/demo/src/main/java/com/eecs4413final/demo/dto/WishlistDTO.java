package com.eecs4413final.demo.dto;

import java.time.LocalDateTime;

public class WishlistDTO {

    private Long wishlistItemID;
    private Long wishlistID;
    private String productName;
    private LocalDateTime addedAt;

    // Constructors
    public WishlistDTO(Long wishlistItemID, Long wishlistID, String productName, LocalDateTime addedAt) {
        this.wishlistItemID = wishlistItemID;
        this.wishlistID = wishlistID;
        this.productName = productName;
        this.addedAt = addedAt;
    }

    public WishlistDTO() {
    }

    // Getters and Setters
    public Long getWishlistItemID() {
        return wishlistItemID;
    }

    public void setWishlistItemID(Long wishlistItemID) {
        this.wishlistItemID = wishlistItemID;
    }

    public Long getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(Long wishlistID) {
        this.wishlistID = wishlistID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
