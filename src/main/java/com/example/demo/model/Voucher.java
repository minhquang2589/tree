package com.example.demo.model;

import java.time.LocalDate;

public class Voucher {
    private int voucherId;
    final private String voucherCode;
    final private double voucherPercentage;
    final private int voucherQuantity;
    final private LocalDate startDate;
    final private LocalDate endDate;
    final private String status;

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

