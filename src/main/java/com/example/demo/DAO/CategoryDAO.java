package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private final Connection connection;

    public CategoryDAO() {
        connection = MySQLConnection.connect();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                String category = resultSet.getString("category");
                String image = resultSet.getString("image");
                String description = resultSet.getString("description");
                categories.add(new Category(categoryId, category, image, description));
            }
        }
        return categories;
    }


    public Category addCategory(Category category) throws SQLException {
        String query = "INSERT INTO categories (category, image, description) VALUES (?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, category.getImage());
            preparedStatement.setString(3, category.getDescription());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setCategoryId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
        return category;
    }


    public Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT * FROM categories WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String categoryName = resultSet.getString("category");
                String image = resultSet.getString("image");
                String description = resultSet.getString("description");
                return new Category(categoryId, categoryName, image, description);
            }
        }
        return null;
    }

    public Category getCategoryByName(String categoryName) throws SQLException {
        Connection connection = MySQLConnection.connect();
        String query = "SELECT * FROM categories WHERE category = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, categoryName);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                String category = resultSet.getString("category");
                String image = resultSet.getString("image");
                String description = resultSet.getString("description");
                return new Category(categoryId, category, image, description);
            }
        }
        return null;
    }
}
