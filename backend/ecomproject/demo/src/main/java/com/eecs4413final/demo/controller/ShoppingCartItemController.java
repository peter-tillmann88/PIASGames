package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.ShoppingCartItemResponseDTO;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.service.ProductService;
import com.eecs4413final.demo.service.ShoppingCartItemService;
import com.eecs4413final.demo.service.ShoppingCartService;
import com.eecs4413final.demo.service.RawCartService;
import com.eecs4413final.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart-items")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = {"Authorization", "Content-Type"})
public class ShoppingCartItemController {

    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartItemService shoppingCartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final RawCartService rawCartService;

    @Autowired
    public ShoppingCartItemController(ShoppingCartService shoppingCartService, ShoppingCartItemService shoppingCartItemService, UserService userService, ProductService productService, RawCartService rawCartService) {
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartItemService = shoppingCartItemService;
        this.userService = userService;
        this.productService = productService;
        this.rawCartService = rawCartService;
    }

    @PostMapping("/cart/{userId}/item/{productId}/add")
    public ResponseEntity<ShoppingCartItemResponseDTO> addItemToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            Product product = productService.getById(productId);
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ShoppingCart cart = shoppingCartService.getCartByUser(userId);
            if (cart == null) {
                System.out.println("Cart for user with ID " + userId + " not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            shoppingCartItemService.addItemToCart(userId, productId, quantity);

            BigDecimal price = product.getPrice();
            BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));

            ShoppingCartItemResponseDTO responseDTO = new ShoppingCartItemResponseDTO(
                    cart.getCartId(),
                    productId,
                    quantity,
                    price
            );

            responseDTO.setTotalPrice(total);
            responseDTO.setMessage("Item added to cart successfully.");

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error occurred while adding item to cart: " + e.getMessage());
            ShoppingCartItemResponseDTO errorResponse = new ShoppingCartItemResponseDTO();
            errorResponse.setMessage("Failed to add item to cart. Please try again.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cart/{userId}/item/{productId}/del")
    public ResponseEntity<ShoppingCartItemResponseDTO> removeItemFromCart(
            @PathVariable Long userId, @PathVariable Long productId) {
        try {
            shoppingCartItemService.removeItemFromCart(userId, productId);
            ShoppingCart cart = shoppingCartService.getCartByUser(userId);
            ShoppingCartItemResponseDTO responseDTO = new ShoppingCartItemResponseDTO(
                    cart.getCartId(),
                    productId,
                    0,
                    productService.getById(productId).getPrice()
            );
            responseDTO.setMessage("Item removed from cart successfully.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/cart/{userId}/item/{productId}/update-quantity")
    public ResponseEntity<String> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer newQuantity) {
        try {
            boolean updated = rawCartService.updateQuantityByUserIdAndProductId(userId, productId, newQuantity);
            if (updated) {
                return new ResponseEntity<>("Quantity updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update quantity. Item not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
