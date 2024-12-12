package com.example.demo.model;

public class SizeQuantity {
    private Size size;
    private int quantity;

    public SizeQuantity(Size size, int quantity) {
        this.size = size;
        this.quantity = quantity;
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
}