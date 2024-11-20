package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.shoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<shoppingCart, Long> {

    shoppingCart findByUserId(Long userId);
}
