package com.eecs4413final.demo.dto;

import java.util.HashSet;
import java.util.Set;
import com.eecs4413final.demo.model.Product;

public class CategoriesResponseDTO {

    private Long categoryId;
    private String name;
    private String description;
    private Set<Product> products = new HashSet<>();
    private String message;

    public CategoriesResponseDTO(Long id, String name, String description) {
        this.categoryId = id;
        this.name = name;
        this.description = description;

    }

    public CategoriesResponseDTO() {
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
