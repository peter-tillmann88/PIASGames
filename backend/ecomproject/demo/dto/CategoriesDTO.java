package com.eecs4413final.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriesDTO {

    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 250, message = "Description must be between 10 and 250 characters")
    private String description;

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
}
