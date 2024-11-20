package com.example.demo.controller;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.loading.LoadingOverlay;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;
import static com.example.demo.controller.HomeController.handleLogin;

public class RegisterController {

    @FXML
    public TextField fullNameField;
    @FXML
    public TextField phoneField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public ComboBox<String> genderComboBox;
    @FXML
    public DatePicker dobDatePicker;
    @FXML
    public TextField emailField;
    @FXML
    public Button registerButton;
    @FXML
    public TextField addressField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    private LoadingOverlay loadingOverlay;
    @FXML
    private StackPane rootPane;

    @FXML
    public void initialize() {
        loadingOverlay = new LoadingOverlay();
        loadingOverlay.addTo(rootPane);
        genderComboBox.getItems().addAll("male", "female", "other");
    }


    public void handleRegister(ActionEvent actionEvent) throws SQLException {
        loadingOverlay.show();
        String fullName = fullNameField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();
        String gender = genderComboBox.getValue();
        String dob = dobDatePicker.getValue() != null ? dobDatePicker.getValue().toString() : "Chưa chọn ngày sinh";
        String email = emailField.getText();
        String address = addressField.getText();

        if (phone.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPass.isEmpty() || gender.isEmpty() || dob.isEmpty() || email.isEmpty() || address.isEmpty()) {
            loadingOverlay.hide();
            Modal.showAlert("Xin vui lòng nhập đủ thông tin và thử lại!");
            return;
        }

        if (!password.equals(confirmPass)) {
            loadingOverlay.hide();
            Modal.showAlert("Mật khẩu và xác nhận mật khẩu không giống nhau. Xin vui lòng nhập lại!");
            return;
        }

        if (Config.isPhoneNumberValid(phone)) {
            loadingOverlay.hide();
            Modal.showAlert("Số điện thoại không hợp lệ!");
            return;
        }

        if (!Config.isEmailValid(email)) {
            loadingOverlay.hide();
            Modal.showAlert("Email không hợp lệ!");
            return;
        }

        boolean isSuccess = insertUser(fullName, phone, Config.hashPassword(password), gender, dob, email, address);
        String hassPass = Config.hashPassword(password);
        loadingOverlay.hide();
        if (isSuccess) {
            UserModel user = handleLogin(phone, hassPass);
            if (user != null) {
                PreferencesUtils.saveUser(user);
                PreferencesUtils.save("isLoggedIn", true);
                handleNavigator(actionEvent, "/com/example/demo/controller/auth/view/admin/dashboard-layout.fxml", true);
            } else {
                handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", false);
            }
        } else {
            Modal.showAlert("Lỗi, xin vui lòng thử lại sau!");
        }
    }

    public static boolean insertUser(String fullName, String phone, String password, String gender, String dob, String email, String address) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ? OR phone = ?";
        String insertSql = "INSERT INTO users (name, phone, password, gender, birthday, email, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = MySQLConnection.connect();

        if (connection != null) {
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, email);
            checkStatement.setString(2, phone);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                Modal.showAlert( "Email hoặc số điện thoại đã tồn tại!");
                return false;
            }
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, fullName);
            insertStatement.setString(2, phone);
            insertStatement.setString(3, password);
            insertStatement.setString(4, gender);
            insertStatement.setString(5, dob);
            insertStatement.setString(6, email);
            insertStatement.setString(7, address);
            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;
        }
        return false;
    }


}

