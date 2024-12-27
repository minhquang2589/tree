package com.example.demo.DAO;
import com.example.demo.config.MySQLConnection;
import javafx.beans.property.IntegerProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDAO {

    public static void insertOrderItem(Connection connection, int orderId, IntegerProperty variantId, int quantity) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, variant_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, variantId.get());
            statement.setInt(3, quantity);
            statement.executeUpdate();
        }
    }


}
