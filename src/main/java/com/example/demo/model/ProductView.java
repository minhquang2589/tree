package com.example.demo.model;

import javafx.beans.property.*;

public class ProductView {
    private StringProperty productName;
    private StringProperty size;
    private IntegerProperty stockQuantity;
    private DoubleProperty price;
    private StringProperty updatedDate;
    private StringProperty discountStatus;
    private StringProperty description;
    private StringProperty isNew;
    private String productImage;


    public ProductView(String productName, String size, int stockQuantity, double price, String updatedDate, String discountStatus, String description, String productImage,String isNew) {
        this.productName = new SimpleStringProperty(productName);
        this.size = new SimpleStringProperty(size);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity);
        this.price = new SimpleDoubleProperty(price);
        this.updatedDate = new SimpleStringProperty(updatedDate);
        this.discountStatus = new SimpleStringProperty(discountStatus);
        this.description = new SimpleStringProperty(description);
        this.isNew = new SimpleStringProperty(isNew);
        this.productImage = productImage;
    }

    public StringProperty productNameProperty() { return productName; }
    public StringProperty sizeProperty() { return size; }
    public IntegerProperty stockQuantityProperty() { return stockQuantity; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty updatedDateProperty() { return updatedDate; }
    public StringProperty discountStatusProperty() { return discountStatus; }
    public StringProperty isNewProperty() { return isNew; }
    public StringProperty descriptionProperty() { return description; }
    public String getProductImage() {
        if (productImage != null && !productImage.isEmpty()) {
            return productImage;
        }
        return null;
    }
}
