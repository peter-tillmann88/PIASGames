package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.shoppingCartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<shoppingCartItems, Long> {

    List<shoppingCartItems> findByShoppingCartId(Long shoppingCartId);

    void deleteAllByCartId(Long id);


}
