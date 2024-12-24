package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String name;
    private boolean isNew;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private int category;

    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.util.Date createdAt;


    public Product(String name, String description, int category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.isNew = false;
    }



    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category.getCategoryId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }



}
