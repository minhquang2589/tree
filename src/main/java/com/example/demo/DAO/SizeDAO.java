package com.example.demo.DAO;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Size;

import java.sql.*;

public class SizeDAO {


    public int addSize(Connection connection,Size size) throws SQLException {
        String query = "INSERT INTO sizes (size, description) VALUES (?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, size.getSize());
            preparedStatement.setString(2, size.getDescription());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int sizeId = generatedKeys.getInt(1);
                        size.setSizeId(sizeId);
                        return sizeId;
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return -1;
    }

    public Size getSizeByName(Connection connection,String sizeText) {
        Size size = null;
        String query = "SELECT * FROM sizes WHERE size = ?";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, sizeText);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int sizeId = resultSet.getInt("size_id");
                String sizeName = resultSet.getString("size");
                String description = resultSet.getString("description");
                size = new Size(sizeId, sizeName, description);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return size;
    }

}
