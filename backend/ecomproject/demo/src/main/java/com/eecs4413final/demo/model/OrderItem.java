package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemID;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    // Default constructor
    public OrderItem() {}

    // Parameterized constructor
    public OrderItem(Order order, Product product, int quantity, BigDecimal priceAtPurchase) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    // Getters and Setters

    public Long getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Long orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    // toString, equals, hashCode
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemID=" + orderItemID +
                ", order=" + order +
                ", product=" + product +
                ", quantity=" + quantity +
                ", priceAtPurchase=" + priceAtPurchase +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(orderItemID, orderItem.orderItemID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemID);
    }
}
