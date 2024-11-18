package com.example.demo.config;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class MySQLConnection {
    private static Connection connection;

    public static Connection connect() {
        if (connection == null) {
            try {
                String url = "jdbc:mariadb://localhost:3306/tree?useSSL=false&serverTimezone=UTC";
                String user = "root";
                String password = "";
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Kết nối database thành công!");
            } catch (SQLException e) {
                System.err.println("Không thể kết nối đến MySQL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đóng kết nối thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Không thể đóng kết nối: " + e.getMessage());
        }
    }
}
