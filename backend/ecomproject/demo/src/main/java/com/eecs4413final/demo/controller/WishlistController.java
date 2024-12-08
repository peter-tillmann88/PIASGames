package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.WishlistDTO;
import com.eecs4413final.demo.model.Wishlist;
import com.eecs4413final.demo.model.WishlistItem;
import com.eecs4413final.demo.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/create/{customerId}")
    public ResponseEntity<Wishlist> createWishlist(@PathVariable Long customerId) {
        Wishlist wishlist = wishlistService.createWishlist(customerId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add/{wishlistId}/{productId}")
    public ResponseEntity<Void> addItemToWishlist(@PathVariable Long wishlistId, @PathVariable Long productId) {
        wishlistService.addItemToWishlist(wishlistId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{wishlistId}/{wishlistItemId}")
    public ResponseEntity<Void> removeItemFromWishlist(@PathVariable Long wishlistId, @PathVariable Long wishlistItemId) {
        wishlistService.removeItemFromWishlist(wishlistId, wishlistItemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items/{wishlistId}")
    public ResponseEntity<List<WishlistDTO>> getWishlistItems(@PathVariable Long wishlistId) {
        List<WishlistItem> items = wishlistService.getWishlistItems(wishlistId);
        List<WishlistDTO> wishlistDTOs = items.stream()
                .map(wishlistService::convertToWishlistDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(wishlistDTOs);
    }
}
