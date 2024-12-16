package com.eecs4413final.demo.dto;

import java.sql.Timestamp;
import java.util.List;

public class ShoppingCartResponseDTO {

    private Long cartId;
    private Long customerId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CartItemDTO> shoppingCartItems;
    private String message;

    public ShoppingCartResponseDTO() {}

    public ShoppingCartResponseDTO(Long cartId, Long customerId, Timestamp createdAt, Timestamp updatedAt, List<CartItemDTO> shoppingCartItems, String message) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.shoppingCartItems = shoppingCartItems;
        this.message = message;
    }

    public static class CartItemDTO {
        private Long cartItemId;
        private String productName;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;

        public CartItemDTO(Long cartItemId, String productName, Integer quantity, Double unitPrice, Double totalPrice) {
            this.cartItemId = cartItemId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
        }

        public Long getCartItemId() {
            return cartItemId;
        }

        public void setCartItemId(Long cartItemId) {
            this.cartItemId = cartItemId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(Double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }
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

    public List<CartItemDTO> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<CartItemDTO> shoppingCartItems){
        this.shoppingCartItems = shoppingCartItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}