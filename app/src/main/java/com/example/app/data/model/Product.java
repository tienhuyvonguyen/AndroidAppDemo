package com.example.app.data.model;


public class Product {
    private String productID;
    private String name;
    private String picture;
    private Double price;
    private Integer stock;

    public Product() {
    }

    public Product(String productID, String name, String picture, Double price, Integer stock) {
        this.productID = productID;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.stock = stock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" + "productID=" + productID + ", name=" + name + ", picture=" + picture + ", price=" + price + ", stock=" + stock + '}';
    }
}
