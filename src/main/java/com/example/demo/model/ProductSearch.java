package com.example.demo.model;

import javafx.beans.property.*;

public class ProductSearch {
    private StringProperty productName;
    private IntegerProperty stockQuantity;
    private DoubleProperty price;
    private StringProperty discountStatus;
    private String productImage;
    private DoubleProperty totalAmount;  // Thành tiền
    private IntegerProperty stt; // Số thứ tự
    private StringProperty category;

    public ProductSearch(String productName, int stockQuantity, double price, String category, String discountStatus , double totalAmount) {
        this.productName = new SimpleStringProperty(productName);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity);
        this.price = new SimpleDoubleProperty(price);
        this.discountStatus = new SimpleStringProperty(discountStatus);
        this.productImage = productImage;
        this.totalAmount = new SimpleDoubleProperty(stockQuantity * price);
        this.stt = new SimpleIntegerProperty(0);
        this.category = new SimpleStringProperty(category);
    }

    public StringProperty productNameProperty() { return productName; }
    public IntegerProperty stockQuantityProperty() { return stockQuantity; }
    public DoubleProperty priceProperty() { return price; }
//    public StringProperty discountStatusProperty() { return discountStatus; }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    public IntegerProperty sttProperty() { return stt; }
    public StringProperty categoryProperty() { return category;}

    public void setStt(int stt) { this.stt.set(stt); }
    public int getStt() { return stt.get(); }

    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }
    public double getTotalAmount() { return totalAmount.get(); }
    public String getProductImage() {
        if (productImage != null && !productImage.isEmpty()) {
            return productImage;
        }
        return null;
    }
}
