package com.eecs4413final.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Products")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Long productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "developer", nullable = false, length = 100)
    private String developer;

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
            name = "product_category",
            joinColumns = @JoinColumn(name = "productid"),
            inverseJoinColumns = @JoinColumn(name = "categoryid")
    )
    private Set<Categories> categoryList = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

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

    public String getDeveloper(){
        return this.developer;
    }

    public void setDeveloper(String developer){
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform(){
        return platform;
    }

    public void setPlatform(String platform){
        this.platform = platform;
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

    public float getSaleMod(){
        return saleMod;
    }

    public void setSaleMod(float saleMod) {
        this.saleMod = saleMod;
    }

    public Set<Categories> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Set<Categories> categoryList) {
        this.categoryList = categoryList;
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

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    

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