// src/main/java/com/eecs4413final/demo/model/Categories.java

package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.util.HashSet;
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

    @ManyToMany(mappedBy = "categoryList")
    private Set<Product> products = new HashSet<>(); // Initialize to prevent NullPointerException

    // Constructors
    public Categories(String name, String description, Set<Product> products) {
        this.name = name;
        this.description = description;
        this.products = products;
    }

    public Categories(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Categories() {
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

    // Helper methods to manage bidirectional relationship
    public void addProduct(Product product){
        this.products.add(product);
        product.getCategoryList().add(this);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
        product.getCategoryList().remove(this);
    }

    @Override
    public String toString() {
        return "Categories{"
            + "categoryID=" + categoryId
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + ", products=" + products
            + '}';
        }
}