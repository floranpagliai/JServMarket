package com.jservmarket.model;

/**
 * com.jservmarket.model in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 04/12/2013 at 03:13
 */

public class ProductsModel {
    private int id_;
    private String designation_;
    private String description_;
    private int category_;
    private float price_;
    private int quantities_;

    public ProductsModel() {
        this.id_ = -1;
        this.designation_ = "";
        this.description_ = "";
        this.category_ = 0;
        this.price_ = 0;
        this.quantities_ = 0;
    }

    public ProductsModel(String designation_, String description_, int category_, float price_, int quantities_) {
        this.id_ = -1;
        this.designation_ = designation_;
        this.description_ = description_;
        this.category_ = category_;
        this.price_ = price_;
        this.quantities_ = quantities_;
    }

    public ProductsModel(int id_, String designation_, String description_, int category_, float price_, int quantities_) {
        this.id_ = id_;
        this.designation_ = designation_;
        this.description_ = description_;
        this.category_ = category_;
        this.price_ = price_;
        this.quantities_ = quantities_;
    }

    public int getId() {
        return id_;
    }

    public void setId(int id) {
        this.id_ = id;
    }

    public String getDesignation() {
        return designation_;
    }

    public void setDesignation(String designation) {
        this.designation_ = designation;
    }

    public String getDescription() {
        return description_;
    }

    public void setDescription(String description) {
        this.description_ = description;
    }

    public int getCategory() {
        return category_;
    }

    public void setCategory(int category) {
        this.category_ = category;
    }

    public float getPrice() {
        return price_;
    }

    public void setPrice(float price) {
        this.price_ = price;
    }

    public int getQuantities() {
        return quantities_;
    }

    public void setQuantities(int quantities) {
        this.quantities_ = quantities;
    }

    @Override
    public String toString() {
        String str =  "|" + this.getId() + ";" + this.getDesignation() + ";";
        if (this.getDescription() != null)
            str += this.getDescription() + ";";
        else
            str += "Pas de description disponible.;";
        str += this.getCategory() + ";" + this.getPrice() + ";" + this.getQuantities();
        return str;
    }
}
