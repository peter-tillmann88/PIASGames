package com.eecs4413final.demo.model;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    // Constructors
    public Image() {
    }

    public Image(String fileName, String fileType, String imageUrl, Product product) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.imageUrl = imageUrl;
        this.product = product;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // ... other getters and setters

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileType(){
        return fileType;
    }

    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    // toString, equals, hashCode
}