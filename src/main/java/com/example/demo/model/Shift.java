package com.example.demo.model;

public class Shift {
    private int t500k = 0;
    private int t200k = 0;
    private int t100k = 0;
    private int t50k = 0;
    private int t20k = 0;
    private int t10k = 0;
    private int t5k = 0;
    private int t2k = 0;
    private int t1k = 0;
    private int t500 = 0;
    private int t200 = 0;
    private int Tong = 0;

    public Shift(int t500k, int t200k, int t100k, int t50k, int t20k, int t10k, int t5k, int t2k, int t1k, int t500, int t200, int tong) {
        this.t500k = t500k;
        this.t200k = t200k;
        this.t100k = t100k;
        this.t50k = t50k;
        this.t20k = t20k;
        this.t10k = t10k;
        this.t5k = t5k;
        this.t2k = t2k;
        this.t1k = t1k;
        this.t500 = t500;
        this.t200 = t200;
        Tong = tong;
    }


    public int getT500k() {
        return t500k;
    }

    public void setT500k(int t500k) {
        this.t500k = t500k;
    }

    public int getT200k() {
        return t200k;
    }

    public void setT200k(int t200k) {
        this.t200k = t200k;
    }

    public int getT100k() {
        return t100k;
    }

    public void setT100k(int t100k) {
        this.t100k = t100k;
    }

    public int getT50k() {
        return t50k;
    }

    public void setT50k(int t50k) {
        this.t50k = t50k;
    }

    public int getT20k() {
        return t20k;
    }

    public void setT20k(int t20k) {
        this.t20k = t20k;
    }

    public int getT10k() {
        return t10k;
    }

    public void setT10k(int t10k) {
        this.t10k = t10k;
    }

    public int getT5k() {
        return t5k;
    }

    public void setT5k(int t5k) {
        this.t5k = t5k;
    }

    public int getT2k() {
        return t2k;
    }

    public void setT2k(int t2k) {
        this.t2k = t2k;
    }

    public int getT1k() {
        return t1k;
    }

    public void setT1k(int t1k) {
        this.t1k = t1k;
    }

    public int getT500() {
        return t500;
    }

    public void setT500(int t500) {
        this.t500 = t500;
    }

    public int getT200() {
        return t200;
    }

    public void setT200(int t200) {
        this.t200 = t200;
    }

    public int getTong() {
        return Tong;
    }

    public void setTong(int tong) {
        Tong = tong;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "t500k=" + t500k +
                ", t200k=" + t200k +
                ", t100k=" + t100k +
                ", t50k=" + t50k +
                ", t20k=" + t20k +
                ", t10k=" + t10k +
                ", t5k=" + t5k +
                ", t2k=" + t2k +
                ", t1k=" + t1k +
                ", t500=" + t500 +
                ", t200=" + t200 +
                ", Tong=" + Tong +
                '}';
    }
}

