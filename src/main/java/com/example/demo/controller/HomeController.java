package com.example.demo.controller;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.MySQLConnection;
import com.example.demo.config.loading.LoadingOverlay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import com.example.demo.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;

public class HomeController {

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField phoneField;
    @FXML
    private StackPane rootPane;

    private LoadingOverlay loadingOverlay;

    @FXML
    public void initialize() {
        loadingOverlay = new LoadingOverlay();
        loadingOverlay.addTo(rootPane);
    }


    @FXML
    public void onRegisterButtonClick(ActionEvent actionEvent) {
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/register-view.fxml", false);
    }


    public void onLoginButtonClick(ActionEvent actionEvent) throws SQLException {
        loadingOverlay.show();
        String phone = phoneField.getText();
        String passWord = passwordField.getText();
        if (phone.isEmpty() || passWord.isEmpty()) {
            loadingOverlay.hide();
            Modal.showAlert("Xin vui lòng điền đủ thông tin và thử lại!");
            return;
        }
        if (Config.isPhoneNumberValid(phone)) {
            loadingOverlay.hide();
            Modal.showAlert("Số điện thoại không hợp lệ!");
            return;
        }
        if (Config.isValidPassword(passWord)) {
            loadingOverlay.hide();
            Modal.showAlert("Mật khẩu không hợp lệ!");
            return;
        }
        UserModel user = handleLogin(phone, Config.hashPassword(passWord));
        if (user != null) {
            PreferencesUtils.saveUser(user);
            PreferencesUtils.save("isLoggedIn", true);
            loadingOverlay.hide();
            handleNavigator(actionEvent, "/com/example/demo/controller/auth/view/admin/dashboard-layout.fxml", true);
        } else {
            loadingOverlay.hide();
            Modal.showAlert("Email, Mật khẩu không đúng hoặc đã xảy ra lỗi. Xin vui lòng thử lại sau!");
        }
    }

    public static UserModel handleLogin(String phone, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE phone = ? AND password = ?";
        Connection connection = MySQLConnection.connect();
        if (connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String fullName = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");
                String birthday = resultSet.getString("birthday");
                String role = resultSet.getString("role");
                String image = resultSet.getString("image");
                String address = resultSet.getString("address");
                return new UserModel(id, fullName, email, phoneNumber, gender, role, birthday, image, password, address);
            }
        }
        return null;
    }

}
