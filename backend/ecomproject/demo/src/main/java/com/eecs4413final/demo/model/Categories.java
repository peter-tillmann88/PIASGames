package com.eecs4413final.demo.model;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Categories")


public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryID")
    private Long categoryId;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Product> products; //set to not allow handle duplicates

    public Categories(String name, String description, Set<Product> products) {
        this.name = name;
        this.description = description;
        this.products = products;
    }
    // Getters and Setters
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

    public void addProducts(Product product){
        this.products.add(product);
    }

    public void removeProducts(Product product){
        this.products.remove(product);
    }
}

