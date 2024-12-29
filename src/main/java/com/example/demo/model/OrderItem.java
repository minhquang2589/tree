package com.example.demo.model;


public class OrderItem {
    private final Integer orderId;
    private final Integer variantId;
    private final String productName;
    private final String productCode;
    private final Integer quantity;
    private final String orderDate;
    private final String productImage;
    private final String status;
    private final String order_reference;
    private final String order_discount;
    private final String order_total_price;
    private final String productSize;
    private final String product_category;

    public OrderItem(Integer orderId, Integer variantId, String productName, String productCode, Integer quantity, String orderDate, String productImage, String status, String order_reference,String order_discount,String order_total_price, String productSize, String pCate) {
        this.orderId = orderId;
        this.variantId = variantId;
        this.productName = productName;
        this.productCode = productCode;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.productImage = productImage;
        this.status = status;
        this.order_reference = order_reference;
        this.order_discount = order_discount;
        this.order_total_price = order_total_price;
        this.productSize = productSize;
        this.product_category = pCate;
    }
    public String getImage() {
        return productImage;
    }
    public String getProductSize() {
        return productSize;
    }

    public String getProduct_size() {
        return productSize;
    }
    public String getProduct_category() {
        return product_category;
    }

    public String getOrder_reference() {
        return order_reference;
    }
    public String getOrder_discount() {
        return order_discount;
    }
    public String getOrder_total_price() {
        return order_total_price;
    }

    public String getStatus() {
        return status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public String getProductName() {
        return productName;
    }



    public String getProductCode() {
        return productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }
}

