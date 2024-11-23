package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "ShoppingCarts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartID")
    private Long cartId;

    @Column(name = "customerID", nullable = false)
    private Long customerId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItems> shoppingCartItems;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    public ShoppingCart(){

    }

    public ShoppingCart(Long customerId, Set<ShoppingCartItems> shoppingCartItems) {
        this.customerId = customerId;
        this.shoppingCartItems = shoppingCartItems;
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

    public void addShoppingCartItems(ShoppingCartItems item){
        this.shoppingCartItems.add(item);
    }

    public void removeShoppingCartItems(ShoppingCartItems item){
        this.shoppingCartItems.remove(item);
    }


}

