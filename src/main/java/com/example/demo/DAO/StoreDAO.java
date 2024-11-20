package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.StoreModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreDAO {
    public boolean addStore(StoreModel store) throws SQLException {
        String sql = "INSERT INTO stores (name, phone, address, start_date, end_date, image, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = MySQLConnection.connect();
        if (connection != null) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, store.getName());
            stmt.setString(2, store.getPhone());
            stmt.setString(3, store.getAddress());
            stmt.setString(4, store.getStartDate());
            stmt.setString(5, null);
            stmt.setString(6, store.getImage());
            stmt.setString(7, store.getStatus());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } else {
            return false;
        }
    }

}
