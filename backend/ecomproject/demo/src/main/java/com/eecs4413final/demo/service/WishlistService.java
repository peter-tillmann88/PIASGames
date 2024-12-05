package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.WishlistDTO;
import com.eecs4413final.demo.model.Wishlist;
import com.eecs4413final.demo.model.WishlistItem;

import java.util.List;

public interface WishlistService {

    // Create a new wishlist for a customer
    Wishlist createWishlist(Long customerId);

    // Add a product to a wishlist
    void addItemToWishlist(Long wishlistId, Long productId);

    // Remove a product from a wishlist
    void removeItemFromWishlist(Long wishlistId, Long wishlistItemId);

    // Get all items in a wishlist
    List<WishlistItem> getWishlistItems(Long wishlistId);

    // Convert WishlistItem to WishlistDTO (optional, depending on the API needs)
    WishlistDTO convertToWishlistDTO(WishlistItem wishlistItem);
}
