package com.example.demo.DAO;
import com.example.demo.config.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMethodDAO {

    public static int findOrInsertPaymentMethod(Connection connection, String methodName, String description) throws SQLException {
        String searchSql = "SELECT payment_method_id FROM payment_methods WHERE method_name = ?";
        try (PreparedStatement searchStatement = connection.prepareStatement(searchSql)) {
            searchStatement.setString(1, methodName);
            try (ResultSet resultSet = searchStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("payment_method_id");
                }
            }
        }

        String insertSql = "INSERT INTO payment_methods (method_name, description) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, methodName);
            insertStatement.setString(2, description);
            insertStatement.executeUpdate();
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert payment method, no ID obtained.");
                }
            }
        }
    }

}

