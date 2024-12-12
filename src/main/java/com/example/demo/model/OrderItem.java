package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemsId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private int quantity;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.util.Date createdAt;

}
