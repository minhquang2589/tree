package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Discount;

import java.sql.*;

public class DiscountDAO {

    public static int addDiscount(Discount discount) throws SQLException {
        Connection connection = MySQLConnection.connect();

        String query = "INSERT INTO discounts (discount_percentage, discount_quantity, discount_remaining, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, discount.getDiscountPercentage());
            preparedStatement.setInt(2, discount.getDiscountQuantity());
            preparedStatement.setInt(3, discount.getDiscountRemaining());
            preparedStatement.setString(4, discount.getStartDate().toString());
            preparedStatement.setString(5, discount.getEndDate().toString());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        discount.setDiscountId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }


    public static void updateDiscountRemaining(Connection connection, String discountId, int quantityPurchased) throws SQLException {
        String sql = "UPDATE discounts " +
                "SET discount_remaining = discount_remaining - ? " +
                "WHERE discount_id = ? AND discount_remaining >= ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantityPurchased);
            statement.setString(2, discountId);
            statement.setInt(3, quantityPurchased);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Discount remaining update failed. Not enough discount remaining available.");
            }
        }
    }




}
