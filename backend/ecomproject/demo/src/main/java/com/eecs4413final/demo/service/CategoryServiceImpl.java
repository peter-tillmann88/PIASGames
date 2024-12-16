package com.eecs4413final.demo.service;

import com.eecs4413final.demo.dto.CategoriesDTO;
import com.eecs4413final.demo.exception.CategoryExistsException;
import com.eecs4413final.demo.exception.CategoryNotFoundException;
import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoryServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Categories getById(Long id) {
        Categories category = categoriesRepository.findById(id).orElse(null);
        if (category != null) {
            return category;
        } else {
            throw new CategoryNotFoundException("Category not found for id: " + id);
        }
    }

    @Override
    public Categories getByName(String name){
        Categories category = categoriesRepository.findByName(name);
        if (category != null) {
            return category;
        } else {
            throw new CategoryNotFoundException("Category not found for name: " + name);
        }
    }

    @Override
    public List<Categories> getAllCategories(){
        return categoriesRepository.findAll();
    }

    @Override
    public Categories addCategory(Categories category){
        if(categoriesRepository.findByName(category.getName()) != null){
            throw new CategoryExistsException("Category already exists with the name: " + category.getName());
        }
        return categoriesRepository.save(category);
    }

    @Override
    public Categories addCategory(CategoriesDTO categoryDTO) {

        if (categoriesRepository.findByName(categoryDTO.getName()) != null) {
            throw new CategoryExistsException("Category already exists with the name: " + categoryDTO.getName());
        }

        Categories newCat = new Categories();
        newCat.setName(categoryDTO.getName());
        newCat.setDescription(categoryDTO.getDescription());

        return categoriesRepository.save(newCat);
    }


    @Override
    public void deleteById(Long id){
        Categories category = categoriesRepository.findById(id).orElse(null);
        if (category != null) {
            categoriesRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException("Category not found for id: " + id);
        }
    }

}