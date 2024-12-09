package com.eecs4413final.demo.dto;

import com.eecs4413final.demo.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CategoriesDTO {

    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 250, message = "Description must be between 10 and 250 characters")
    private String description;

    private Set<Product> products;

    // Getters and Setters

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

    public Set<Product>  getProducts(){
        return products;
    }

    public void setProducts(Set<Product> products){
        this.products = products;
    }
}
