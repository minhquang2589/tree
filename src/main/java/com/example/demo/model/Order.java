package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    private double discount;
    private double totalPrice;

    @Column(name = "order_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private String status;
    private String orderReference;
    private String note;

}
