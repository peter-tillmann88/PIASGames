package com.eecs4413final.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private int orderID;
    private LocalDateTime orderDate;
    private String status;
    private double totalAmount;
    private List<OrderItemDTO> orderItems;

    public OrderDTO() {}

    public OrderDTO(int orderID, LocalDateTime orderDate, String status, double totalAmount, List<OrderItemDTO> orderItems) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public LocalDateTime getOrderDate(){
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate){
        this.orderDate = orderDate;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public double getTotalAmount(){
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount){
        this.totalAmount = totalAmount;
    }

    public List<OrderItemDTO> getOrderItems(){
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems){
        this.orderItems = orderItems;
    }
}
