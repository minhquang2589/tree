package com.example.demo.DAO;

import com.example.demo.model.Product;

import java.io.File;
import java.sql.*;
import java.util.List;

import static com.example.demo.Utils.Config.saveImage;

public class ProductDAO {


    public void addProduct(Connection connection, Product product) throws SQLException {
        String query = "INSERT INTO products (name, is_new, category_id, description) VALUES (?,?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBoolean(2, product.isNew());
            preparedStatement.setInt(3, product.getCategory());
            preparedStatement.setString(4, product.getDescription());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public void updateProduct(Connection connection, Product product, String productId) throws SQLException {
        String query = "UPDATE products SET name = ?, is_new = ?, category_id = ?, description = ? WHERE product_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBoolean(2, product.isNew());
            preparedStatement.setInt(3, product.getCategory());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setString(5, productId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating product failed, no rows affected.");
            }
        }
    }


    public void addProductImages(Connection connection, int productId, List<String> imagePaths) throws SQLException {
        String query = "INSERT INTO images (image, product_id) VALUES (?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (String imagePath : imagePaths) {
                String savedFilePath = saveImage(imagePath, new File(imagePath));
                System.out.println(savedFilePath);
                preparedStatement.setString(1, savedFilePath);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();
            }
        }
    }


    public void updateProductImageById(Connection connection, String imageId, String newImagePath) throws SQLException {
        String query = "UPDATE images SET image = ? WHERE image_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String savedFilePath = saveImage(newImagePath, new File(newImagePath));
            preparedStatement.setString(1, savedFilePath);
            preparedStatement.setString(2, imageId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating image failed, no rows affected.");
            }
        }
    }


}
