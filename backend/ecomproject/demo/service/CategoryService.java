package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.CategoriesDTO;
import com.eecs4413final.demo.model.Categories;


import java.util.List;
import java.util.Set;

public interface CategoryService {

    Categories getById(Long id);
    Categories getByName(String name);
    List<Categories> getAllCategories();
    Categories addCategory(Categories category);
    Categories addCategory(CategoriesDTO category);
    void deleteById(Long id);
}
