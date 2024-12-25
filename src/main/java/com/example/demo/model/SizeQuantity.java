package com.example.demo.model;

public class SizeQuantity {
    private Size size;
    private int quantity;
    private int price;

    public SizeQuantity(Size size, int quantity, int price) {
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}