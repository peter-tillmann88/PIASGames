package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.CartIdDTO;
import com.eecs4413final.demo.dto.ShoppingCartResponseDTO;
import com.eecs4413final.demo.exception.ShoppingCartNotFoundException;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.service.RawCartService;
import com.eecs4413final.demo.service.ShoppingCartService;
import com.eecs4413final.demo.mapper.ShoppingCartMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final RawCartService rawCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, RawCartService rawCartService) {
        this.shoppingCartService = shoppingCartService;
        this.rawCartService = rawCartService;
    }


    @GetMapping("/{userId}/cart")
    public ResponseEntity<List<Map<String, Object>>> getAllProducts(@PathVariable Long userId) {
        try {
            String cartItemsJson = rawCartService.getCartItemsByUserId(userId);
            if (cartItemsJson == null || cartItemsJson.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Parse the JSON string into a List of Maps
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> cartItems = objectMapper.readValue(
                    cartItemsJson, new TypeReference<List<Map<String, Object>>>() {}
            );

            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/cartId")
    public ResponseEntity<CartIdDTO> getCartIdByUserId(@PathVariable Long userId) {
        try {
            ShoppingCart cart = shoppingCartService.getCartByUser(userId);
            CartIdDTO cartIdDTO = new CartIdDTO(cart.getCartId());
            cartIdDTO.setMessage("Cart ID retrieved successfully for user ID: " + userId);
            return new ResponseEntity<>(cartIdDTO, HttpStatus.OK);
        } catch (ShoppingCartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long cartItemId) {
        try {
            boolean isDeleted = rawCartService.deleteCartItem(cartItemId);
            if (isDeleted) {
                return ResponseEntity.ok("Cart item deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the cart item.");
        }
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<String> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            int newQuantity = (int) requestBody.get("quantity");

            boolean isUpdated = rawCartService.updateCartItemQuantity(cartItemId, newQuantity);
            if (isUpdated) {
                return ResponseEntity.ok("Cart item quantity updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the cart item.");
        }
    }


    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ShoppingCartResponseDTO> clearCart(@PathVariable Long userId) {
        try {
            ShoppingCart cart = shoppingCartService.getCartByUser(userId);
            shoppingCartService.clearCart(cart.getCartId());
            ShoppingCart updatedCart = shoppingCartService.getCart(cart.getCartId());
            ShoppingCartResponseDTO responseDTO = ShoppingCartMapper.toResponseDTO(updatedCart, "Cart cleared successfully for user ID: " + userId);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ShoppingCartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}