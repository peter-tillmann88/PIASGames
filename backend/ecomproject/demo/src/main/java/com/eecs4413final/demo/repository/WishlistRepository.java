package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    // Add custom queries if needed
}
