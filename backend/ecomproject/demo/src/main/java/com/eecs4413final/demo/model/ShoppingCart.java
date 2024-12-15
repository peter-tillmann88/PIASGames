package com.eecs4413final.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "shopping_carts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cartId")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartid")
    private Long cartId;

    @Column(name = "customerid", nullable = false)
    private Long customerId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItems> shoppingCartItems;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public ShoppingCart() {}

    public ShoppingCart(Long customerId, Set<ShoppingCartItems> shoppingCartItems) {
        this.customerId = customerId;
        this.shoppingCartItems = shoppingCartItems;
    }

    public ShoppingCart(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt){
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt){
        this.updatedAt = updatedAt;
    }

    public Set<ShoppingCartItems> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(Set<ShoppingCartItems> shoppingCartItems){
        this.shoppingCartItems = shoppingCartItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void addShoppingCartItems(ShoppingCartItems item){
        this.shoppingCartItems.add(item);
    }

    public void removeShoppingCartItems(ShoppingCartItems item){
        this.shoppingCartItems.remove(item);
    }
}