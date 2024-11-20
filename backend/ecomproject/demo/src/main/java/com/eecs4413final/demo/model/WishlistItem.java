package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlistitems", uniqueConstraints = @UniqueConstraint(columnNames = {"wishlistID", "productID"}))
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistItemID;

    @ManyToOne
    @JoinColumn(name = "wishlistID", nullable = false)
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;

    // Default constructor
    public WishlistItem() {
        this.addedAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public WishlistItem(Wishlist wishlist, Product product) {
        this.wishlist = wishlist;
        this.product = product;
        this.addedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getWishlistItemID() {
        return wishlistItemID;
    }

    public void setWishlistItemID(Long wishlistItemID) {
        this.wishlistItemID = wishlistItemID;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    // toString method

    @Override
    public String toString() {
        return "WishlistItem{" +
                "wishlistItemID=" + wishlistItemID +
                ", wishlistID=" + wishlist.getWishlistID() +
                ", product=" + product.getName() +
                ", addedAt=" + addedAt +
                '}';
    }
}