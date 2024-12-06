package com.eecs4413final.demo.dto;

import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product developer is required")
    private String developer;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Product price is required")
    private BigDecimal price;

    @NotNull(message = "Product stock is required")
    private Integer stock;

    @NotNull(message = "Product categories are required")
    @Size(min = 1, message = "At least one category is required")
    private Set<Categories> categories;

    private float saleMod;

    @NotBlank(message = "Product platform is required")
    private String platform;

    private List<ImageDTO> images;

    // Getters and Setters

    public Long getId(){
        return  id;
    }

    public void setId(Long id){
        this.id = id;
    }
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

    public Set<Categories> getCategory(){
        return categories;
    }

    public void addCategory(Categories categories){
        this.categories.add(categories);
    }
}
