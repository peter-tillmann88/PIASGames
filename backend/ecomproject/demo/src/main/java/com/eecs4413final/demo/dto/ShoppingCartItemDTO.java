package com.eecs4413final.demo.dto;

import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.ShoppingCart;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ShoppingCartItemDTO {

    private Long shoppingCartId;
    private Long productId;
    private Integer quantity;
//    private BigDecimal unitPrice;
//    private BigDecimal totalPrice;
    private String message;

    // Constructors
    public ShoppingCartItemDTO() {
    }

    public ShoppingCartItemDTO(Long shoppingCartId, Long productId, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice, String message) {
        this.shoppingCartId = shoppingCartId;
        this.productId = productId;
        this.quantity = quantity;
//        this.unitPrice = unitPrice;
//        this.totalPrice = totalPrice;
        this.message = message;
    }

    // Getters and Setters
    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

//    public BigDecimal getUnitPrice() {
//        return unitPrice;
//    }
//
//    public void setUnitPrice(BigDecimal unitPrice) {
//        this.unitPrice = unitPrice;
//    }
//
//    public BigDecimal getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(BigDecimal totalPrice) {
//        this.totalPrice = totalPrice;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
