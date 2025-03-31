package com.example.lab2_w10;

// Dish.java
public class Dish {
    private String name;
    private int price;
    private int imageRes;

    public Dish(String name, int price, int imageRes) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
    }

    // Getter methods
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getImageRes() { return imageRes; }
}