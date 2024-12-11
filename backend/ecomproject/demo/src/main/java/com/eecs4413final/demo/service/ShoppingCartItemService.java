package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.ShoppingCartItems;

import java.util.List;

public interface ShoppingCartItemService {

    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    ShoppingCartItems getCartItem(Long cartId, Long productId);
}
