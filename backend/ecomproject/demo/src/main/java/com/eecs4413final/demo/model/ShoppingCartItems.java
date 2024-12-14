package com.eecs4413final.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "shoppingcartitems")
public class ShoppingCartItems {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemID")
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cartid",referencedColumnName = "cartid", nullable = false)
    @JsonIgnore
    private ShoppingCart shoppingCart;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "added_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp addedAt;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public ShoppingCartItems(){

    }

    public ShoppingCartItems(ShoppingCart shoppingCart, Product product, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
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

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
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
    @Override public String toString() {
        return "ShoppingCartItems{"
                + "cartItemID=" + cartItemId
                + ", shoppingCartID=" + shoppingCart
                + ", product=" + product
                + ", quantity=" + quantity
                + ", addedAt=" + addedAt
                + ", unitPrice=" + unitPrice
                + ", totalPrice=" + totalPrice
                + '}';
    }

}
