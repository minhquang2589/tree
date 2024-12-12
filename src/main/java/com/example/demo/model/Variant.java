package com.example.demo.model;

public class Variant {

    private int variantId;
    private int productId;
    private int sizeId;
    private int quantity;
    private int discountId;

    public Variant(int variantId, int productId, int sizeId, int quantity, int discountId) {
        this.variantId = variantId;
        this.productId = productId;
        this.sizeId = sizeId;
        this.quantity = quantity;
        this.discountId = discountId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
}
