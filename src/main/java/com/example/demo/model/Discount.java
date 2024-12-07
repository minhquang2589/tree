package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int discountId;

    @Column(name = "discount_percentage")
    private double discountPercentage;

    @Column(name = "discount_quantity")
    private int discountQuantity;

    @Column(name = "discount_remaining")
    private int discountRemaining;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "updated_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
