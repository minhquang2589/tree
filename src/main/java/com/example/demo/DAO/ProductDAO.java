package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Product;
import com.example.demo.model.Size;

import java.sql.*;
import java.util.List;

public class ProductDAO {

    private final Connection connection;

    public ProductDAO() {
         connection = MySQLConnection.connect();
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, is_new, category_id, description) VALUES (?, ?, ?, ?)";
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            preparedStatement.setString(1, product.getName());
//            preparedStatement.setBoolean(2, product.isNew());
//            preparedStatement.setInt(3, product.getCategory().getCategoryId());
//            preparedStatement.setString(4, product.getDescription());
//
//            int affectedRows = preparedStatement.executeUpdate();
//            if (affectedRows > 0) {
//                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        product.setProductId(generatedKeys.getInt(1));
//                    }
//                }
//            }
//        }
    }

    public void addProductImages(int productId, List<String> imagePaths) throws SQLException {
        String query = "INSERT INTO images (image, product_id) VALUES (?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (String imagePath : imagePaths) {
                preparedStatement.setString(1, imagePath);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();
            }
        }
    }

    public void addProductSize(int productId, Size size) throws SQLException {
        String query = "INSERT INTO variants (product_id, size_id, quantity) VALUES (?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, size.getSizeId());
            preparedStatement.setInt(3, size.getQuantity());

            preparedStatement.executeUpdate();
        }
    }
}
