package com.example.demo.controller;

import com.google.gson.*;
import com.example.demo.model.ProductSearch;

import java.lang.reflect.Type;

public class ProductSearchAdapter implements JsonSerializer<ProductSearch>, JsonDeserializer<ProductSearch> {

    @Override
    public JsonElement serialize(ProductSearch product, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("stt", product.getStt());
        jsonObject.addProperty("tenSanPham", product.getTenSanPham());
        jsonObject.addProperty("loai", product.getLoai());
        jsonObject.addProperty("size", product.getSize());
        jsonObject.addProperty("gia", product.getGia());
        jsonObject.addProperty("soLuong", product.getSoLuong());
        jsonObject.addProperty("chietKhau", product.getChietKhau());
        jsonObject.addProperty("thanhTien", product.getThanhTien());
        jsonObject.addProperty("image", product.getImage());
        jsonObject.addProperty("variantId", product.variantIdProperty().get());
        jsonObject.addProperty("discountId", product.discountIdProperty());
        jsonObject.addProperty("productId", product.getProductId());
        jsonObject.addProperty("code", product.getCode().get());
        jsonObject.addProperty("productDescription", product.getProductDescription());
        jsonObject.addProperty("sizeId", product.getSizeId());
        jsonObject.addProperty("categoryId", product.getCategoryId());
        jsonObject.addProperty("imageId", product.getImageId());
        jsonObject.addProperty("productQuantity", product.getProductQty());
        jsonObject.addProperty("discountRemaining", product.getDiscountRemaining());

        return jsonObject;
    }

    @Override
    public ProductSearch deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int stt = jsonObject.has("stt") ? jsonObject.get("stt").getAsInt() : 0;
        String tenSanPham = jsonObject.has("tenSanPham") ? jsonObject.get("tenSanPham").getAsString() : "";
        String loai = jsonObject.has("loai") ? jsonObject.get("loai").getAsString() : "";
        String image = jsonObject.has("image") ? jsonObject.get("image").getAsString() : "";
        double gia = jsonObject.has("gia") ? jsonObject.get("gia").getAsDouble() : 0.0;
        int soLuong = jsonObject.has("soLuong") ? jsonObject.get("soLuong").getAsInt() : 0;
        double chietKhau = jsonObject.has("chietKhau") ? jsonObject.get("chietKhau").getAsDouble() : 0.0;
        double thanhTien = jsonObject.has("thanhTien") ? jsonObject.get("thanhTien").getAsDouble() : 0.0;
        String size = jsonObject.has("size") ? jsonObject.get("size").getAsString() : "";
        int vId = jsonObject.has("variantId") ? jsonObject.get("variantId").getAsInt() : 0;
        String discountId = jsonObject.has("discountId") ? jsonObject.get("discountId").getAsString() : "";
        String productId = jsonObject.has("productId") ? jsonObject.get("productId").getAsString() : "";
        String code = jsonObject.has("code") ? jsonObject.get("code").getAsString() : "";
        String productDescription = jsonObject.has("productDescription") ? jsonObject.get("productDescription").getAsString() : "";
        String sizeId = jsonObject.has("sizeId") ? jsonObject.get("sizeId").getAsString() : "";
        String categoryId = jsonObject.has("categoryId") ? jsonObject.get("categoryId").getAsString() : "";
        String imageId = jsonObject.has("imageId") ? jsonObject.get("imageId").getAsString() : "";
        boolean isNew = jsonObject.has("isNew") ? jsonObject.get("isNew").getAsBoolean() : false;
        String qty = jsonObject.has("productQuantity") ? jsonObject.get("productQuantity").getAsString() : "";
        String discountRemaining = jsonObject.has("discountRemaining") ? jsonObject.get("discountRemaining").getAsString() : "";
        ProductSearch productSearch = new ProductSearch(stt, tenSanPham, image, loai, gia, soLuong, chietKhau, thanhTien, size, vId, discountId, productId, code, productDescription, sizeId, categoryId, imageId, isNew, qty, discountRemaining);
        return productSearch;
    }

}
