package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "return_products")
public class ReturnProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int returnId;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private String returnReason;

    @Column(name = "return_time", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnTime;

}
