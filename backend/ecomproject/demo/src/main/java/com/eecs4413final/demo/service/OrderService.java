package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.Order;
import java.util.List;

public interface OrderService {
    Order createOrderFromCart(Long userId);
    List<Order> getOrdersForUser(Long userId);
    List<Order> getAllOrders();
}
