package com.example.demo.DAO;

import java.sql.*;
public class VariantDAO {


    public void addProductVariant(Connection connection,int productId, int sizeId,int price, int quantity, String code, String discount_id) throws SQLException {
        String query = "INSERT INTO variants (product_id, size_id,price, quantity, code, discount_id) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, sizeId);
            preparedStatement.setInt(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, code);
            preparedStatement.setString(6, discount_id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int variantId = generatedKeys.getInt(1);
                    }
                }
            }
        }
    }

    public static void updateProductVariant(Connection connection,int variantId, int quantity) throws SQLException {
        String checkQuery = "SELECT quantity FROM variants WHERE variant_id = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, variantId);
            ResultSet rs = checkStatement.executeQuery();
            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                if (currentQuantity >= Math.abs(quantity)) {
                    String query = "UPDATE variants SET quantity = quantity + ? WHERE variant_id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, quantity);
                        preparedStatement.setInt(2, variantId);
                        preparedStatement.executeUpdate();
                    }
                } else {
                    throw new SQLException("Không đủ số lượng sản phẩm để cập nhật.");
                }
            }
        }
    }
}
