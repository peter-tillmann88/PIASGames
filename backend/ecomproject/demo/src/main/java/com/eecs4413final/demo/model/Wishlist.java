package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false) // Change to userID
    private User user; // Replace Customer with User

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WishlistItem> wishlistItems;

    public Wishlist() {
        this.createdAt = LocalDateTime.now();
    }

    public Wishlist(User user, String name) {
        this.user = user;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public Long getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(Long wishlistID) {
        this.wishlistID = wishlistID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(Set<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }
}
