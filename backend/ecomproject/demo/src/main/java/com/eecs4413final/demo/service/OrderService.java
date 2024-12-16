package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.Order;

public interface OrderService {
    Order createOrderFromCart(Long userId);
}
