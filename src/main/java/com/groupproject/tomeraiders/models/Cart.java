package com.groupproject.tomeraiders.models;

import lombok.Data;

@Data
public class Cart {

    private int id;
    private String title;
    private String price;
    private int quantity;
    private String image;

    public Cart(int id, String title, String price, int quantity, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

}