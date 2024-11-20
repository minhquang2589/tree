package com.example.demo.model;

public class StoreModel {
    private final int store_id;
    private String name = "";
    private String phone;
    private String address = "";
    private String startDate = "";
    private String endDate = "";
    private String image = "";
    private String status = "";


    public StoreModel(int store_id, String name, String phone, String address, String startDate, String endDate, String image) {
        this.store_id = store_id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return store_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Store{store_id=%d, name='%s', phone='%s', address='%s', startDate='%s', endDate='%s', image='%s', status='%s'}",
                store_id, name, phone, address, startDate, endDate, image, status);
    }

    public void setName(String value) {
        this.name = value;
    }

    public void setPhone(String value) {
        this.phone = value;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public void setStartDate(String value) {
        this.startDate = value;
    }

    public void setEndDate(String value) {
        this.endDate = value;
    }

    public void setImage(String value) {
        this.image = value;
    }

    public void setStatus(String value) {
        this.status = value;
    }
}
