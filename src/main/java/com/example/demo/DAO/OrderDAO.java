package com.example.demo.DAO;

import com.example.demo.model.Voucher;

import java.sql.*;


public class OrderDAO {


    public static int insertOrder(Connection connection, String customerId, int paymentMethodId, double totalPrice, String note, double discount, String orderCode, Voucher voucher) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, payment_method_id, total_price, note, discount,order_reference, voucher_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customerId);
            statement.setInt(2, paymentMethodId);
            statement.setDouble(3, totalPrice);
            statement.setString(4, note);
            statement.setDouble(5, discount);
            statement.setString(6, orderCode);
            if (voucher != null) {
                statement.setInt(7, voucher.getVoucherId());
            } else {
                statement.setString(7, null);
            }
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
    }


}
