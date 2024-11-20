package com.eecs4413final.demo.repository;

import com.eecs4413final.demo.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Categories findByName(String name);

    boolean existsByName(String name);


}
