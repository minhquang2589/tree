package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Size;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SizeDAO {

    private final Connection connection;

    public SizeDAO() {

        this.connection = MySQLConnection.connect();

    }

    public List<Size> getAllSizes() throws SQLException {
        List<Size> sizes = new ArrayList<>();
        String query = "SELECT * FROM sizes";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

//            while (resultSet.next()) {
//                int sizeId = resultSet.getInt("size_id");
//                String size = resultSet.getString("size");
//                String description = resultSet.getString("description");
//                sizes.add(new Size(sizeId, size, description));
//            }
        }
        return sizes;
    }

    public void addSize(Size size) throws SQLException {
        String query = "INSERT INTO sizes (size, description) VALUES (?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, size.getSize());
            preparedStatement.setString(2, size.getDescription());

            preparedStatement.executeUpdate();
        }
    }

//    public Size getSizeById(int sizeId) throws SQLException {
//        String query = "SELECT * FROM sizes WHERE size_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setInt(1, sizeId);
//            ResultSet resultSet = stmt.executeQuery();
//            if (resultSet.next()) {
//                String sizeName = resultSet.getString("size");
//                String description = resultSet.getString("description");
//                return new Size(sizeId, sizeName, description);
//            }
//        }
//        return null;
//    }
}
