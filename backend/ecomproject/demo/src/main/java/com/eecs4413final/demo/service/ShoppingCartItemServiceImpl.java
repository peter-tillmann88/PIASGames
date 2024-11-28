package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.ShoppingCartItems;
import com.eecs4413final.demo.repository.ShoppingCartItemRepository;

public class ShoppingCartItemServiceImpl implements ShoppingCartItemService{

    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartItemServiceImpl(ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        
    }

    @Override
    public void remvoveItemFromCart(Long cartId, Long productId, int quantity) {

    }

    @Override
    public void remvoveItemFromCart(Long cartId, Long productId) {

    }

    @Override
    public ShoppingCartItems getCartItem(Long cartId, Long productId) {
        return null;
    }
}
