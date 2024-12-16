package com.eecs4413final.demo.dto;

import java.math.BigDecimal;

public class ProductUpdateDTO {
    private String name;
    private String developer;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Float saleMod;
    private String platform;

   

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Float getSaleMod() {
        return saleMod;
    }

    public void setSaleMod(Float saleMod) {
        this.saleMod = saleMod;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
