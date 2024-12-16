package com.eecs4413final.demo.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long orderItemID;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal priceAtPurchase;

    public OrderItemDTO() {}

    public OrderItemDTO(Long orderItemID, Long productId, String productName, int quantity, BigDecimal priceAtPurchase) {
        this.orderItemID = orderItemID;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Long getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Long orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}