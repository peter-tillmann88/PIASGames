// src/main/java/com/eecs4413final/demo/model/Product.java

package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private Long productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "developer", nullable = false, length = 100)
    private String developer;   //added developer field

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "platform", nullable = false, length = 100)
    private String platform;


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
    private Set<Categories> categoryList = new HashSet<>(); // Initialize to prevent NullPointerException

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //removes all images when item deleted
    private List<Image> images = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime  createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructors
    public Product(){
        this.createdAt = LocalDateTime.now();
    }

    public Product(String name, String developer, String description, BigDecimal price, int stock, float saleMod, Set<Categories> categories, List<Image> images, String platform) {
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

    public Product(String name, String developer, String description, BigDecimal price, int stock, float saleMod, Set<Categories> categories, String platform) {
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

    public String getDeveloper(){
        return this.developer;
    }

    public void setDeveloper(String developer){
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

    // Helper methods to manage bidirectional relationship
    public void addCategory(Categories category) {
        this.categoryList.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Categories category){
        this.categoryList.remove(category);
        category.getProducts().remove(this);
    }

    public void addImages(Image image) {
        this.images.add(image);
        image.setProduct(this);
    }

    public void removeImages(Image image){
        this.images.remove(image);
        image.setProduct(null);
    }

    public void setMod(float mod){
        this.saleMod = mod;
    }

    public float getMod(){
        return saleMod;
    }

    public void setPlatform(String platform){
        this.platform = platform;
    }

    public String getPlatform(){
        return platform;
    }

    // toString, equals, hashCode
    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productId +
                ", name='" + name + '\'' +
                ", developer='" + developer + '\'' +
                ", description='" + description + '\'' +
                ", platform='" + platform + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", saleMod=" + saleMod +
                ", categoryList=" + categoryList +
                ", images=" + images +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}