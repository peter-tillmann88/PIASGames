package com.eecs4413final.demo.service;

import com.eecs4413final.demo.exception.ShoppingCartNotFoundException;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.model.ShoppingCartItems;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.repository.ShoppingCartRepository;
import com.eecs4413final.demo.repository.UserRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{


    private final ShoppingCartRepository shoppingCartRepository;


    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;

    }


    @Override
    public ShoppingCart getCart(Long cartId) {

        ShoppingCart shoppingcart = shoppingCartRepository.findById(cartId).orElse(null);
        if(shoppingcart != null){
            return shoppingcart;
        }else{
            throw new ShoppingCartNotFoundException("Shopping cart not found with id: "+ cartId);
        }
    }

    @Override
    public void clearCart(Long cartId) {
        ShoppingCart cart = getCart(cartId);
        cart.getShoppingCartItems().clear();
        shoppingCartRepository.deleteById(cartId);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        ShoppingCart shoppingcart = shoppingCartRepository.findById(cartId).orElse(null);
        Set<ShoppingCartItems> items;
        if (shoppingcart != null) {
            items = shoppingcart.getShoppingCartItems();
        } else {
            throw new ShoppingCartNotFoundException("Shopping cart not found with id: " + cartId);
        }
        BigDecimal total = BigDecimal.ZERO;
        for (ShoppingCartItems item : items) {
            total = total.add(item.getTotalPrice());
        }
        return total;
    }


    @Override
    public ShoppingCart getCartByUser(Long userId) {
        return shoppingCartRepository.findByUser_UserId(userId);
    }

    @Override
    public ShoppingCart makeNewCart(Long userId){
        ShoppingCart cart = new ShoppingCart(userId);
        cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        shoppingCartRepository.save(cart);
        return cart;
    }

}
