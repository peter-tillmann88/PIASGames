package com.eecs4413final.demo.controller;

import com.eecs4413final.demo.dto.CategoriesDTO;
import com.eecs4413final.demo.dto.CategoriesResponseDTO;
import com.eecs4413final.demo.dto.ObjectResponseDTO;
import com.eecs4413final.demo.exception.CategoryNotFoundException;
import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ObjectResponseDTO> getAllCategories(){
        try{
            List<Categories> categories = categoryService.getAllCategories();
            ObjectResponseDTO response = new ObjectResponseDTO("Found", categories);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<CategoriesResponseDTO> addCategory(@Valid @RequestBody CategoriesDTO category) {
        try {
            Categories newCat = categoryService.addCategory(category);
            CategoriesResponseDTO responseDTO = new CategoriesResponseDTO(
                    newCat.getCategoryId(),
                    newCat.getName(),
                    newCat.getDescription()
//                    newCat.getProducts()
            );
            responseDTO.setMessage("Category successfully added.");
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            CategoriesResponseDTO errorResponse = new CategoriesResponseDTO();
            errorResponse.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<CategoriesResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoriesResponseDTO responseDTO = new CategoriesResponseDTO();
        try {
            Categories category = categoryService.getById(id);
            responseDTO.setCategoryId(category.getCategoryId());
            responseDTO.setName(category.getName());
            responseDTO.setDescription(category.getDescription());
            responseDTO.setProducts(category.getProducts());
            responseDTO.setMessage("Category found.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            responseDTO.setMessage("Category not found for id: " + id);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/{name}")
    public ResponseEntity<CategoriesResponseDTO> getCategoryByName(@PathVariable String name) {
        CategoriesResponseDTO responseDTO = new CategoriesResponseDTO();
        try {
            Categories category = categoryService.getByName(name);
            responseDTO.setCategoryId(category.getCategoryId());
            responseDTO.setName(category.getName());
            responseDTO.setDescription(category.getDescription());
            responseDTO.setProducts(category.getProducts());
            responseDTO.setMessage("Category found.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            responseDTO.setMessage("Category not found for name: " + name);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<CategoriesResponseDTO> deleteCategoryById(@PathVariable Long id) {
        CategoriesResponseDTO responseDTO = new CategoriesResponseDTO();
        try {
            categoryService.deleteById(id);
            responseDTO.setMessage("Category successfully deleted.");
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            responseDTO.setMessage("Category not found for id: " + id);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseDTO.setMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}