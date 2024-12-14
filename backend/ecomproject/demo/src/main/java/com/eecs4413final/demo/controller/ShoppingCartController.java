package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ProductDTO;
import com.eecs4413final.demo.dto.ProductResponseDTO;
import com.eecs4413final.demo.dto.ShoppingCartResponseDTO;
import com.eecs4413final.demo.exception.ProductNotFoundException;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.model.ShoppingCartItems;
import com.eecs4413final.demo.service.ProductService;
import com.eecs4413final.demo.service.ShoppingCartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{userId}/cart")
    public ResponseEntity<ShoppingCartResponseDTO> getAllProducts(@PathVariable Long userId) {
        ShoppingCartResponseDTO responseDTO = null;
        try {
            ShoppingCart cart = shoppingCartService.getCartByUser(userId);
            if(cart.getCartId() != null){
                responseDTO = new ShoppingCartResponseDTO(
                        cart.getCartId(),
                        cart.getCustomerId(),
                        cart.getCreatedAt(),
                        cart.getUpdatedAt(),
                        cart.getShoppingCartItems());
                responseDTO.setMessage("Cart found for user id: " + userId);
            }

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ShoppingCartResponseDTO> clearCart( @PathVariable Long userId) {
        ShoppingCartResponseDTO responseDTO = null;
        try {
            shoppingCartService.clearCart(userId);
            ShoppingCart cart = shoppingCartService.getCartByUser(userId);
            responseDTO = new ShoppingCartResponseDTO(
                    cart.getCartId(),
                    cart.getCustomerId(),
                    cart.getCreatedAt(),
                    cart.getUpdatedAt(),
                    cart.getShoppingCartItems());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
