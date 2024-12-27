package com.example.demo.model;
import javafx.beans.property.*;

public class ProductSearch {
    private IntegerProperty stt;
    private StringProperty tenSanPham;
    private StringProperty loai;
    private DoubleProperty gia;
    private StringProperty size;
    private IntegerProperty soLuong;
    private DoubleProperty chietKhau;
    private DoubleProperty thanhTien;
    private String image;
    private IntegerProperty variantId;
    private String discountId;


    public ProductSearch(int stt, String tenSanPham, String image, String loai, double gia, int soLuong, double chietKhau, double thanhTien, String size, int vId,String discountId) {
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

    public String discountIdProperty() {
        return discountId;
    }

    public void setVariantId(int id) {
        this.variantId.set(id);
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

    public StringProperty loaiProperty() {
        return loai;
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
