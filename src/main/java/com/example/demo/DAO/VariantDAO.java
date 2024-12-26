package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Variant;

import java.sql.*;

public class VariantDAO {

    private final Connection connection;

    public VariantDAO() {
        connection = MySQLConnection.connect();
    }

    public void addProductVariant(int productId, int sizeId,int price, int quantity, String code, int discount_id) throws SQLException {
        String query = "INSERT INTO variants (product_id, size_id,price, quantity, code, discount_id) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, sizeId);
            preparedStatement.setInt(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, code);
            preparedStatement.setInt(6, discount_id);

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

    public void updateProductVariant(int variantId, int quantity) throws SQLException {
        String query = "UPDATE variants SET quantity = ? WHERE variant_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, variantId);

            preparedStatement.executeUpdate();
        }
    }

    public Variant getVariant(int productId, int sizeId) throws SQLException {
        String query = "SELECT * FROM variants WHERE product_id = ? AND size_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, sizeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int variantId = resultSet.getInt("variant_id");
                    int quantity = resultSet.getInt("quantity");
                    int discountId = resultSet.getInt("discount_id");
                    int price = resultSet.getInt("price");
                    String code = resultSet.getNString("code");
                    return new Variant(variantId, productId, sizeId, quantity, price,discountId, code);
                }
            }
        }
        return null;
    }

    public void deleteVariant(int variantId) throws SQLException {
        String query = "DELETE FROM variants WHERE variant_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, variantId);

            preparedStatement.executeUpdate();
        }
    }
}
