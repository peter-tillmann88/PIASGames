package com.eecs4413final.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AdminOrderDTO extends OrderDTO {
    private String username;

    public AdminOrderDTO() {
        super();
    }

    public AdminOrderDTO(int orderID, LocalDateTime orderDate, String status, double totalAmount, List<OrderItemDTO> orderItems, String username) {
        super(orderID, orderDate, status, totalAmount, orderItems);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
