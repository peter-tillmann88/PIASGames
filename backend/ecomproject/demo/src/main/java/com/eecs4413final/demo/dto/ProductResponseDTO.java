// src/main/java/com/eecs4413final/demo/model/Product.java

package com.eecs4413final.demo.dto;

import com.eecs4413final.demo.model.Categories;
import com.eecs4413final.demo.model.Image;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductResponseDTO {

    private Long productId;

    private String name;

    private String developer;   //added developer field

    private String description;

    private String platform;

    private BigDecimal price;

    private int stock;

    private float saleMod;

    private Set<Categories> categoryList = new HashSet<>(); // Initialize to prevent NullPointerException

    private List<Image> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String message;

    // Constructors
    public ProductResponseDTO() {
        this.createdAt = LocalDateTime.now();
    }

    public ProductResponseDTO(Long id, String name, String developer, String description, BigDecimal price, int stock, float saleMod, Set<Categories> categories, List<Image> images, String platform) {
        this.productId = id;
        this.name = name;
        this.developer = developer;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.saleMod = saleMod;
        this.categoryList = categories;
        this.images = images;
        this.createdAt = LocalDateTime.now();
        this.platform = platform;
    }

    public ProductResponseDTO(Long id,String name, String developer, String description, BigDecimal price, int stock, float saleMod, Set<Categories> categories, String platform) {
        this.productId = id;
        this.name = name;
        this.developer = developer;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.saleMod = saleMod;
        this.categoryList = categories;
        this.createdAt = LocalDateTime.now();
        this.platform = platform;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getDeveloper() {
        return this.developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Set<Categories> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Set<Categories> categoryList) {
        this.categoryList = categoryList;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer quantity) {
        this.stock = quantity;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }


    public void setMod(float mod) {
        this.saleMod = mod;
    }

    public float getMod() {
        return saleMod;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
