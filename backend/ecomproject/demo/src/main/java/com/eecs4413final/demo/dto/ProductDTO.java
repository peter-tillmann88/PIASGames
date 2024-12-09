package com.eecs4413final.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {

    @NotBlank(message = "Product developer is required")
    private String developer;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    private BigDecimal price;

    @NotNull(message = "Product stock is required")
    @Min(value = 0, message = "Product stock must be non-negative")
    private int stock;

    private float saleMod;

    @NotBlank(message = "Product platform is required")
    private String platform;

    @NotNull(message = "Product categories are required")
    private List<Long> categoryIds; // Changed from List<Categories> to List<Long>

    private List<String> imageNames;

    // Getters and Setters

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getSaleMod() {
        return saleMod;
    }

    public void setSaleMod(float saleMod) {
        this.saleMod = saleMod;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }
}