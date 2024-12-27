package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public static int findOrInsertCustomer(Connection connection, String email, String phone, String address) throws SQLException {
        String searchSql = "SELECT customer_id, total_purchase FROM customers WHERE email = ? OR phone = ?";
        try (PreparedStatement searchStatement = connection.prepareStatement(searchSql)) {
            searchStatement.setString(1, email);
            searchStatement.setString(2, phone);
            try (ResultSet resultSet = searchStatement.executeQuery()) {
                if (resultSet.next()) {
                    int customerId = resultSet.getInt("customer_id");
                    int currentTotalPurchase = resultSet.getInt("total_purchase");

                    String updateSql = "UPDATE customers SET total_purchase = ? WHERE customer_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                        updateStatement.setInt(1, currentTotalPurchase + 1);
                        updateStatement.setInt(2, customerId);
                        updateStatement.executeUpdate();
                    }

                    return customerId;
                }
            }
        }

        String insertSql = "INSERT INTO customers (email, phone, address) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, email);
            insertStatement.setString(2, phone);
            insertStatement.setString(3, address);
            insertStatement.executeUpdate();

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert customer, no ID obtained.");
                }
            }
        }
    }


    ;
}
