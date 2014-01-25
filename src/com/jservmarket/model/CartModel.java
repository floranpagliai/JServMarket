package com.jservmarket.model;

public class CartModel {
    private int id_;
    private int userId_;
    private int productId_;
    private int quantity_;
    private String designation_;
    private float price_;

    public CartModel() {
        this.id_ = -1;
        this.productId_ = -1;
        this.userId_ = -1;
        this.quantity_ = -1;
        this.designation_ = "";
        this.price_ = -1;
    }

    public CartModel(int userId_, int productId_, int quantity_) {
        this.id_ = -1;
        this.userId_ = userId_;
        this.productId_ = productId_;
        this.quantity_ = quantity_;
        this.designation_ = "";
        this.price_ = -1;
    }

    public CartModel(int id_, int userId_, int productId_, int quantity_, String designation_, float price_) {
        this.id_ = id_;
        this.productId_ = productId_;
        this.userId_ = userId_;
        this.quantity_ = quantity_;
        this.designation_ = designation_;
        this.price_ = price_;
    }

    public int getId() {
        return id_;
    }

    public void setId(int id) {
        this.id_ = id;
    }

    public int getUserId() {
        return userId_;
    }

    public void setUserId(int userId) {
        this.userId_ = userId;
    }

    public int getProductId() {
        return productId_;
    }

    public void setProductId(int productId) {
        this.productId_ = productId;
    }

    public int getQuantity() {
        return quantity_;
    }

    public void setQuantity(int quantity) {
        this.quantity_ = quantity;
    }

    public String getDesignation() {
        return designation_;
    }

    public void setDesignation_(String designation) {
        this.designation_ = designation;
    }

    public float getPrice() {
        return price_;
    }

    public void setPrice(int price) {
        this.price_ = price;
    }

    @Override
    public String toString() {
        String str = "|" + this.getId() + ";" + this.getProductId() + ";" + this.getQuantity() + ";";
        str += this.getDesignation() + ";" + this.getPrice();

        return str;
    }
}
