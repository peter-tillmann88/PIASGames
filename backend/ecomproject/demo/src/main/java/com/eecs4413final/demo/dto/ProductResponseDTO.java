package com.eecs4413final.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ProductResponseDTO {

    private Long productId;
    private String name;
    private String developer;
    private String description;
    private String platform;
    private BigDecimal price;
    private int stock;
    private float saleMod;
    private Set<CategoriesResponseDTO> categoryList;
    private List<ImageDTO> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;


    public ProductResponseDTO() {
        this.createdAt = LocalDateTime.now();
    }


    public ProductResponseDTO(
            Long productId, String name, String developer, String description, BigDecimal price,
            int stock, float saleMod, Set<CategoriesResponseDTO> categoryList, List<ImageDTO> images,
            String platform) {
        this.productId = productId;
        this.name = name;
        this.developer = developer;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.saleMod = saleMod;
        this.categoryList = categoryList;
        this.images = images;
        this.createdAt = LocalDateTime.now();
        this.platform = platform;
    }


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

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public Set<CategoriesResponseDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Set<CategoriesResponseDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
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

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}