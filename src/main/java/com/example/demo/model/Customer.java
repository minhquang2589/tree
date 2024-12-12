package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(name = "total_purchase", columnDefinition = "int default 0")
    private int totalPurchase;

    @Column(name = "total_use_voucher", columnDefinition = "int default 0")
    private int totalUseVoucher;

    @Column(name = "total_use_discount", columnDefinition = "int default 0")
    private int totalUseDiscount;

    private String email;
    private String phone;
    private String address;

    @Column(name = "updated_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
