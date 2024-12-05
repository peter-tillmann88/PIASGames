package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    // This method uses the correct path for accessing wishlistID in the Wishlist entity
    List<WishlistItem> findByWishlist_wishlistID(Long wishlistID);
}
