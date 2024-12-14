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
    List<Product> findByCategoryList_Name(String categoryName);
    List<Product> findByPlatform(String platform);
    List<Product> findByDeveloperAndPlatform(String developer, String platform);
    List<Product> findByCategoryList_NameAndPlatform(String categoryName, String platform);
    List<Product> findByPlatformAndName(String platform, String name);

    boolean existsByName(String name);

    // Correctly placed method without nesting
    List<Product> findByNameContainingIgnoreCase(String name);
}
