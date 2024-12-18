package com.example.demo.DAO;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Product;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Config.saveImage;

public class ProductDAO {

    private final Connection connection;

    public ProductDAO() {
        connection = MySQLConnection.connect();
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, price, is_new, category_id, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setBoolean(3, product.isNew());
            preparedStatement.setInt(4, product.getCategory());
            preparedStatement.setString(5, product.getDescription());

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


    public void addProductImages(int productId, List<String> imagePaths) throws SQLException {
        String query = "INSERT INTO images (image, product_id) VALUES (?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (String imagePath : imagePaths) {
                String savedFilePath = saveImage(imagePath, new File(imagePath));
                preparedStatement.setString(1, savedFilePath);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();
            }
        }
    }


}
