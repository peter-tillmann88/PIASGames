package com.eecs4413final.demo.service;

import com.eecs4413final.demo.exception.ShoppingCartNotFoundException;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.model.ShoppingCartItems;
import com.eecs4413final.demo.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart getCart(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found with id: " + cartId));
    }

    @Override
    public void clearCart(Long cartId) {
        ShoppingCart cart = getCart(cartId);
        cart.getShoppingCartItems().clear();
        shoppingCartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        ShoppingCart cart = getCart(cartId);
        return cart.getShoppingCartItems().stream()
                .map(ShoppingCartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public ShoppingCart getCartByUser(Long userId) {
        return shoppingCartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found for user ID: " + userId));
    }

    @Override
    public ShoppingCart makeNewCart(Long userId) {
        ShoppingCart cart = new ShoppingCart(userId);
        cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        shoppingCartRepository.save(cart);
        return cart;
    }
}
