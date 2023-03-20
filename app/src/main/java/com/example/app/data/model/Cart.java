package com.example.app.data.model;

public class Cart {

    private String productID;
    private Double productPrice;
    private Integer productQuantity;

    public Cart() {
    }

    public Cart(String productID, Double productPrice, Integer productQuantity) {
        this.productID = productID;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Double getTotalPrice() {
        return productPrice * productQuantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "productID='" + productID + '\'' +
                ", productPrice=" + productPrice +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
