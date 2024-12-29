package com.example.demo.model;

import javafx.beans.property.*;
import com.google.gson.annotations.Expose;

public class ProductSearch {
    @Expose
    private IntegerProperty stt;
    @Expose
    private StringProperty tenSanPham;
    @Expose
    private StringProperty loai;
    @Expose
    private DoubleProperty gia;
    @Expose
    private StringProperty size;
    @Expose
    private IntegerProperty soLuong;
    @Expose
    private DoubleProperty chietKhau;
    @Expose
    private DoubleProperty thanhTien;
    @Expose
    private String image;
    @Expose
    private IntegerProperty variantId;
    @Expose
    private String discountId;
    @Expose
    private String productId;
    @Expose
    private StringProperty code;
    @Expose
    private String productDescription;
    @Expose
    private String sizeId;
    @Expose
    private String categoryId;
    @Expose
    private String imageId;
    @Expose
    private String productQuantity;
    @Expose
    private String discountRemaining;
    @Expose
    private boolean isNew;


    public ProductSearch(int stt, String tenSanPham, String image, String loai, double gia, int soLuong, double chietKhau, double thanhTien, String size, int vId, String discountId, String productId, String code, String productDescription, String sizeId, String categoryId, String imageId, boolean isNew, String qty, String discountRemaining) {
        this.stt = new SimpleIntegerProperty(stt);
        this.tenSanPham = new SimpleStringProperty(tenSanPham);
        this.loai = new SimpleStringProperty(loai);
        this.size = new SimpleStringProperty(size);
        this.gia = new SimpleDoubleProperty(gia);
        this.soLuong = new SimpleIntegerProperty(soLuong);
        this.chietKhau = new SimpleDoubleProperty(chietKhau);
        this.thanhTien = new SimpleDoubleProperty(thanhTien);
        this.image = image;
        this.variantId = new SimpleIntegerProperty(vId);
        this.discountId = discountId;
        this.productId = productId;
        this.imageId = imageId;
        this.sizeId = sizeId;
        this.categoryId = categoryId;
        this.productDescription = productDescription;
        this.code = new SimpleStringProperty(code);
        this.isNew = isNew;
        this.productQuantity = qty;
        this.discountRemaining = discountRemaining;

    }

    public String getImage() {
        if (image != null && !image.isEmpty()) {
            return image;
        }
        return null;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStt() {
        return stt.get();
    }

    public IntegerProperty sttProperty() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt.set(stt);
    }

    public IntegerProperty variantIdProperty() {
        return variantId;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public String discountIdProperty() {
        return discountId;
    }

    public String getProductQty() {
        return productQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getDiscountRemaining() {
        return discountRemaining;
    }

    public String getSizeId() {
        return sizeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public StringProperty getCode() {
        return code;
    }

    public void setVariantId(int id) {
        this.variantId.set(id);
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public String getTenSanPham() {
        return tenSanPham.get();
    }

    public StringProperty tenSanPhamProperty() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham.set(tenSanPham);
    }

    public String getLoai() {
        return loai.get();
    }

    public String getProductDescription() {
        return productDescription;
    }

    public StringProperty loaiProperty() {
        return loai;
    }

    public String getImageId() {
        return imageId;
    }


    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }


    public void setLoai(String loai) {
        this.loai.set(loai);
    }

    public double getGia() {
        return gia.get();
    }

    public DoubleProperty giaProperty() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia.set(gia);
    }

    public int getSoLuong() {
        return soLuong.get();
    }

    public IntegerProperty soLuongProperty() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong.set(soLuong);
    }

    public double getChietKhau() {
        return chietKhau.get();
    }

    public DoubleProperty chietKhauProperty() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        this.chietKhau.set(chietKhau);
    }

    public double getThanhTien() {
        return thanhTien.get();
    }

    public DoubleProperty thanhTienProperty() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien.set(thanhTien);
    }
}
