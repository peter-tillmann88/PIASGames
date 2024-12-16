package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.ShoppingCartItems;

public interface ShoppingCartItemService {
    void addItemToCart(Long userId, Long productId, int quantity);
    void removeItemFromCart(Long userId, Long productId, int quantity);
    void removeItemFromCart(Long userId, Long productId);
    ShoppingCartItems getCartItem(Long userId, Long productId);
}
