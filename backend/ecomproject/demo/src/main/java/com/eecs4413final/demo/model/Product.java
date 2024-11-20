package com.eecs4413final.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "developer", nullable = false, length = 100)
    private String developer;   //added developer field

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;


    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "salePercent")
    private float saleMod;

    @ManyToMany
    @JoinTable(
            name = "Product_Categories",
            joinColumns = @JoinColumn(name = "productID"),
            inverseJoinColumns = @JoinColumn(name = "categoryID"))
    private Set<Categories> categoryList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //removes all images when item deleted
    private List<Image> images;


    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;


    public Product(String name, String developer, String description, BigDecimal price, int stock, float saleMod, Set<Categories> categories, List<Image> images) {
        this.name = name;
        this.developer = developer;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.saleMod = saleMod;
        this.categoryList = categories;
        this.images = images;
    }
    // Getters and Setters
    public Long getProductId() {
        return id;
    }

    public void setProductId(Long productId) {
        this.id = productId;
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

    public String getDeveloper(){
        return this.developer;
    }

    public void setDeveloper(String developer){
        this.developer = developer;
    }

    public Set<Categories> getCategory() {
        return categoryList;
    }

    public Set<Categories> setCategory(Set<Categories> categoryList) {
        return this.categoryList = categoryList;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addCategory(Categories category) {
        this.categoryList.add(category);
    }

    public void removeCategory(Categories category){
        this.categoryList.remove(category);
    }

    public void addImages(Image images) {
        this.images.add(images);
    }

    public void removeImages(Image images){
        this.images.remove(images);
    }


}

