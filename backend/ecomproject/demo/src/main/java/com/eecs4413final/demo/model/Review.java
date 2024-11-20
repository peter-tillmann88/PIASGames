package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customerID", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewDate;

    // Default constructor
    public Review() {
        this.reviewDate = LocalDateTime.now();
    }

    // Parameterized constructor
    public Review(Product product, Customer customer, Integer rating, String comment) {
        this.product = product;
        this.customer = customer;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    // toString method

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", product=" + product.getName() +
                ", customer=" + customer.getFirstName() + " " + customer.getLastName() +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}
