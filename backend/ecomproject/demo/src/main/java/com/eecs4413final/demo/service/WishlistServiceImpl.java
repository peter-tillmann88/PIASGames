package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.WishlistDTO;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.User;
import com.eecs4413final.demo.model.Wishlist;
import com.eecs4413final.demo.model.WishlistItem;
import com.eecs4413final.demo.repository.ProductRepository;
import com.eecs4413final.demo.repository.UserRepository;
import com.eecs4413final.demo.repository.WishlistItemRepository;
import com.eecs4413final.demo.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               WishlistItemRepository wishlistItemRepository,
                               ProductRepository productRepository,
                               UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Wishlist createWishlist(Long userId) {
        // Fetch the User by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Create and initialize a new Wishlist
        Wishlist wishlist = new Wishlist(user, "My Wishlist");

        return wishlistRepository.save(wishlist);
    }

    @Override
    public void addItemToWishlist(Long wishlistId, Long productId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found with ID: " + wishlistId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        WishlistItem wishlistItem = new WishlistItem(wishlist, product);
        wishlistItemRepository.save(wishlistItem);
    }

    @Override
    public void removeItemFromWishlist(Long wishlistId, Long wishlistItemId) {
        WishlistItem wishlistItem = wishlistItemRepository.findById(wishlistItemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found with ID: " + wishlistItemId));

        if (!wishlistItem.getWishlist().getWishlistID().equals(wishlistId)) {
            throw new RuntimeException("Item does not belong to the specified wishlist");
        }

        wishlistItemRepository.delete(wishlistItem);
    }

    @Override
    public List<WishlistItem> getWishlistItems(Long wishlistId) {
        wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found with ID: " + wishlistId));

        return wishlistItemRepository.findByWishlist_wishlistID(wishlistId);
    }

    @Override
    public WishlistDTO convertToWishlistDTO(WishlistItem wishlistItem) {
        return new WishlistDTO(
                wishlistItem.getWishlistItemID(),
                wishlistItem.getWishlist().getWishlistID(),
                wishlistItem.getProduct().getName(),
                wishlistItem.getAddedAt()
        );
    }

    @Override
    public List<WishlistItem> getWishlistItemsByUserId(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findAll().stream()
                .filter(wishlist -> wishlist.getUser().getUserId().equals(userId))
                .toList();

        return wishlists.stream()
                .flatMap(wishlist -> wishlistItemRepository.findByWishlist_wishlistID(wishlist.getWishlistID()).stream())
                .collect(Collectors.toList());
    }
}
