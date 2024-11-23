package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    List<Product> findByDeveloper(String developer);

    List<Product> findByCategoryListIn(Set<Categories> categories);

    boolean existsByName(String name);
}
