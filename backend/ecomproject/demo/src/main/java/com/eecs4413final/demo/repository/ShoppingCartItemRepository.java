package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.ShoppingCartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItems, Long> {
    List<ShoppingCartItems> findByShoppingCart_CartId(Long shoppingCartId);
    void deleteAllByShoppingCart_CartId(Long shoppingCartId);
}
