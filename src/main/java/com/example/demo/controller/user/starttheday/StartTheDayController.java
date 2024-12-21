package com.example.demo.controller.user.starttheday;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.Utils.Modal;
import com.example.demo.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.demo.config.MySQLConnection.connect;

public class StartTheDayController {
    @FXML
    public Text currenttime;

    LocalDateTime currentTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDateTime = currentTime.format(formatter);

    public void initialize() throws IOException {
        currenttime.setText(formattedDateTime);
    }

    public boolean check_day() throws IOException {
        Connection connection = connect();
        String checkQuery = "SELECT COUNT(*) FROM shifts WHERE DATE(start_date) = '" + formattedDateTime + "'";
        Statement checkStatement = null;
        ResultSet resultSet = null;
        try {
            checkStatement = connection.createStatement();
            resultSet = checkStatement.executeQuery(checkQuery);
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return false;
            }
            else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void startday() throws IOException {
        Connection connection = connect();

        if (connection != null) {
            User user = PreferencesUtils.getUser();
            assert user != null;

            String query = "INSERT INTO shifts (user_id, start_date) VALUES (" + user.getId() + ", '" + LocalDateTime.now() + "')";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void onStartTheDaySuccess(ActionEvent actionEvent) throws IOException {
        startday();
        Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/startthedaysuccess/startthedaysuccess.fxml", "",null);
    }
}
