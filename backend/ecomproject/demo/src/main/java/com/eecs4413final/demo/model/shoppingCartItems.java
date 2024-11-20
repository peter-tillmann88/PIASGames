package com.eecs4413final.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "ShoppingCartItems")
public class shoppingCartItems {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemID")
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cartID", nullable = false)
    private shoppingCart shoppingCart;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "added_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp addedAt;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public shoppingCartItems(){

    }

    public shoppingCartItems(shoppingCart shoppingCart, Product product, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.shoppingCart = shoppingCart;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.addedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public shoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(shoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
