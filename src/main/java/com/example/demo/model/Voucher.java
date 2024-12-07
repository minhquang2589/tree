package com.example.demo.model;
import javax.persistence.*;
import java.util.Date;
import java.time.LocalDate;
@Entity
@Table(name = "vouchers")

public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voucherId;
    @Column(name = "voucher_code", unique = true)
    private String voucherCode;

    @Column(name = "voucher_percentage")
    private double voucherPercentage;

    @Column(name = "voucher_quantity")
    private int voucherQuantity;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String status;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Voucher(int voucherId, String voucherCode, double voucherPercentage, int voucherQuantity,
                   LocalDate startDate, LocalDate endDate, String status) {
        this.voucherId = voucherId;
        this.voucherCode = voucherCode;
        this.voucherPercentage = voucherPercentage;
        this.voucherQuantity = voucherQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public double getVoucherPercentage() {
        return voucherPercentage;
    }

    public int getVoucherQuantity() {
        return voucherQuantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
}

