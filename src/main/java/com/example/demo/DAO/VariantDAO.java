package com.example.demo.DAO;

import java.sql.*;

public class VariantDAO {


    public void addProductVariant(Connection connection, String productId, int sizeId, String price, int quantity, String code, String discount_id) throws SQLException {
        String query = "INSERT INTO variants (product_id, size_id,price, quantity, code, discount_id) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, productId);
            preparedStatement.setInt(2, sizeId);
            preparedStatement.setString(3, price);
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

    public static void updateProductVariant(Connection connection, String productId, String sizeId, String price, int quantity, String discountId, boolean isBuy) throws SQLException {
        String checkQuery = "SELECT quantity FROM variants WHERE product_id = ? AND size_id = ?";

        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, productId);
            checkStatement.setString(2, sizeId);
            ResultSet rs = checkStatement.executeQuery();
            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                if (currentQuantity + quantity < 0) {
                    throw new SQLException("Không đủ số lượng sản phẩm để cập nhật.");
                }
                String updateQuery = """
                            UPDATE variants
                            SET quantity = ?, price = ?, discount_id = ?
                            WHERE product_id = ? AND size_id = ?
                        """;
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setInt(1, isBuy ? (currentQuantity - quantity) : quantity);
                    preparedStatement.setString(2, price);
                    preparedStatement.setString(3, discountId);
                    preparedStatement.setString(4, productId);
                    preparedStatement.setString(5, sizeId);
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

}
