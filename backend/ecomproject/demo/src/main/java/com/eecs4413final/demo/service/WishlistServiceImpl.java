package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.WishlistDTO;
import com.eecs4413final.demo.model.Customer;
import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.Wishlist;
import com.eecs4413final.demo.model.WishlistItem;
import com.eecs4413final.demo.repository.CustomerRepository;
import com.eecs4413final.demo.repository.ProductRepository;
import com.eecs4413final.demo.repository.WishlistItemRepository;
import com.eecs4413final.demo.repository.WishlistRepository;
import com.eecs4413final.demo.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               WishlistItemRepository wishlistItemRepository,
                               ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Wishlist createWishlist(Long customerId) {
        // Fetch the Customer by ID using the default findById method from JpaRepository
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create and initialize a new Wishlist
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer); // Set the Customer object instead of just the ID
        wishlist.setName("Default Wishlist Name"); // You can modify this logic to accept user input for name
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void addItemToWishlist(Long wishlistId, Long productId) {
        Optional<Wishlist> wishlistOpt = wishlistRepository.findById(wishlistId);
        if (wishlistOpt.isPresent()) {
            Wishlist wishlist = wishlistOpt.get();
            // Retrieve the product (make sure the `Product` entity exists in your database)
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            WishlistItem wishlistItem = new WishlistItem(wishlist, product);
            wishlistItemRepository.save(wishlistItem);
        } else {
            throw new RuntimeException("Wishlist not found");
        }
    }

    @Override
    public void removeItemFromWishlist(Long wishlistId, Long wishlistItemId) {
        Optional<WishlistItem> wishlistItemOpt = wishlistItemRepository.findById(wishlistItemId);
        if (wishlistItemOpt.isPresent() && wishlistItemOpt.get().getWishlist().getWishlistID().equals(wishlistId)) {
            wishlistItemRepository.delete(wishlistItemOpt.get());
        } else {
            throw new RuntimeException("Item not found in the wishlist");
        }
    }

    @Override
    public List<WishlistItem> getWishlistItems(Long wishlistId) {
        return wishlistItemRepository.findByWishlist_wishlistID(wishlistId);
    }

    // Method to convert WishlistItem to WishlistDTO
    @Override
    public WishlistDTO convertToWishlistDTO(WishlistItem wishlistItem) {
        return new WishlistDTO(
                wishlistItem.getWishlistItemID(),
                wishlistItem.getWishlist().getWishlistID(),
                wishlistItem.getProduct().getName(),
                wishlistItem.getAddedAt()
        );
    }
}
