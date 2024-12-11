package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.ShoppingCart;

import java.math.BigDecimal;

public interface ShoppingCartService {

    ShoppingCart getCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);
    ShoppingCart getCartByUser(Long userId);
    ShoppingCart makeNewCart(Long userId);

}
