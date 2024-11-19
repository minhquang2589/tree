package com.example.demo.DAO;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<UserModel> getAllUsers() throws SQLException {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        Connection connection = MySQLConnection.connect();
        PreparedStatement checkStatement = connection.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("user_id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String gender = rs.getString("gender");
            String role = rs.getString("role");
            String birthday = rs.getString("birthday");
            String image = rs.getString("image");
            String password = rs.getString("password");
            String address = rs.getString("address");
            users.add(new UserModel(id, name, email, phone, gender, role, birthday, image,password,address));
        }
        return users;
    }


    public String deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        Connection connection = null;
        try {
            connection = MySQLConnection.connect();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return "User with ID " + userId + " has been deleted.";
            } else {
                return "No user found with ID " + userId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public void updateUser(UserModel user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, gender = ?, role = ?, image = ? WHERE user_id = ?";
        Connection connection = MySQLConnection.connect();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getGender());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getImage());
            stmt.setInt(7, user.getId());
            stmt.executeUpdate();
        }
    }


}
