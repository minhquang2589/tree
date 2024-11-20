package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.StoreModel;
import com.example.demo.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<StoreModel> getAllStore() throws SQLException {
        List<StoreModel> store = new ArrayList<>();
        String query = "SELECT * FROM stores";
        Connection connection = MySQLConnection.connect();
        PreparedStatement checkStatement = connection.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("store_id");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String start = rs.getString("start_date");
            String end = rs.getString("end_date");
            String image = rs.getString("image");
            String status = rs.getString("status");
            store.add(new StoreModel(id, name, phone, address, start, end, image, status));
        }
        return store;

    }
}

