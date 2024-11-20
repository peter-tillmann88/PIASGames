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
    @JoinColumn(name = "customerID", nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WishlistItem> wishlistItems;

    // Default constructor
    public Wishlist() {
        this.createdAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public Wishlist(Customer customer, String name) {
        this.customer = customer;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(Long wishlistID) {
        this.wishlistID = wishlistID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(Set<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    // toString method

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistID=" + wishlistID +
                ", customer=" + customer.getFirstName() + " " + customer.getLastName() +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}