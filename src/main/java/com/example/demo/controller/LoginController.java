package com.example.demo.controller;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.MySQLConnection;
import com.example.demo.config.button.ButtonHandler;
import com.example.demo.config.loading.LoadingOverlay;
import com.example.demo.model.User;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;

public class LoginController {

    @FXML
    private TextField fp_inputemail;

    @FXML
    private AnchorPane fp_inputform;

    @FXML
    private TextField fp_inputuser;
    @FXML
    private PasswordField np_comfirmPassword;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;


    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_phone;

    @FXML
    private Button side_CreateBtn;

    @FXML
    private Button side_alreadyHave;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_address;

    @FXML
    private PasswordField su_comfirmpassword;

    @FXML
    private DatePicker su_date;

    @FXML
    private ComboBox<String> su_gender;

    @FXML
    private PasswordField su_password;

    @FXML
    private TextField su_phone;
    @FXML
    private TextField su_email;

    @FXML
    private TextField su_username;

    @FXML
    private StackPane rootPane;

    private LoadingOverlay loadingOverlay;

    public void initialize() {
        loadingOverlay = new LoadingOverlay();
        loadingOverlay.addTo(rootPane);
        su_gender.getItems().addAll("male", "female", "other");
    }

    public static String handleCheckRole(User user) throws SQLException {
        boolean isLoggedIn = (boolean) PreferencesUtils.get("isLoggedIn", false);
        if (isLoggedIn && user != null) {
            System.out.println(user.toString());
            String role = user.getRole();
            return switch (role) {
                case "user" ->
                        "/com/example/demo/controller/auth/view/user/salesdashboardlayout/sales-dashboard-layout.fxml";
                case "admin" -> "/com/example/demo/controller/auth/view/admin/dashboard-layout.fxml";
                default -> "/com/example/demo/controller/auth/login-view.fxml";
            };
        }
        return "/com/example/demo/controller/auth/login-view.fxml";
    }


    public void handleLogin(ActionEvent actionEvent) throws SQLException {
        loadingOverlay.show();
        String phone = si_phone.getText();
        String passWord = si_password.getText();
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
        User user = login(phone, Config.hashPassword(passWord));
        if (user != null) {
            PreferencesUtils.saveUser(user);
            PreferencesUtils.save("isLoggedIn", true);
            loadingOverlay.hide();
            handleNavigator(actionEvent, handleCheckRole(user), true);
        } else {
            loadingOverlay.hide();
            Modal.showAlert("Số điện thoại, Mật khẩu không đúng hoặc đã xảy ra lỗi. Xin vui lòng thử lại sau!");
        }

    }

    public static User login(String phone, String password) throws SQLException {
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
                return new User(id, fullName, email, phoneNumber, gender, role, birthday, image, password, address);
            }
        }
        return null;
    }

    public void handleSignup(ActionEvent actionEvent) throws SQLException {
        loadingOverlay.show();
        String fullName = su_username.getText();
        String phone = su_phone.getText();
        String password = su_password.getText();
        String confirmPass = su_comfirmpassword.getText();
        String gender = su_gender.getValue();
        String dob = su_date.getValue() != null ? su_date.getValue().toString() : "Chưa chọn ngày sinh";
        String email = su_email.getText();
        String address = su_address.getText();

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
            User user = login(phone, hassPass);
            if (user != null) {
                PreferencesUtils.saveUser(user);
                PreferencesUtils.save("isLoggedIn", true);
                handleNavigator(actionEvent, handleCheckRole(user), true);
            } else {
                handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", false);
            }
        } else {
            Modal.showAlert("Lỗi, xin vui lòng thử lại sau!");
        }
    }

    public static boolean insertUser(String fullName, String phone, String password, String gender, String dob, String email, String address) throws SQLException {
        String countQuery = "SELECT COUNT(*) AS user_count FROM users";
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ? OR phone = ?";
        String insertSql = "INSERT INTO users (name, phone, password, gender, birthday, email, address, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = MySQLConnection.connect();

        if (connection != null) {
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            ResultSet countResult = countStatement.executeQuery();
            int userCount = 0;
            if (countResult.next()) {
                userCount = countResult.getInt("user_count");
            }

            String role = (userCount == 0) ? "admin" : "user";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, email);
            checkStatement.setString(2, phone);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                Modal.showAlert("Email hoặc số điện thoại đã tồn tại. Vui lòng đăng nhập!");
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
            insertStatement.setString(8, role);

            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;
        }
        return false;
    }


    public void handleForgotPassword(ActionEvent actionEvent) throws SQLException {
        loadingOverlay.show();
        String phone = fp_inputuser.getText();
        String email = fp_inputemail.getText();

        if (phone.isEmpty() || email.isEmpty()) {
            loadingOverlay.hide();
            Modal.showAlert("Xin vui lòng nhập số điện thoại và email!");
            return;
        }

        Connection connection = MySQLConnection.connect();
        String query = "SELECT * FROM users WHERE phone = ? AND email = ?";
        if (connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loadingOverlay.hide();
                Modal.showAlert("Tìm thấy tài khoản! Xin vui lòng tiếp tục thay đổi mật khẩu.");
                fp_inputform.setVisible(false);
                np_newPassForm.setVisible(true);
            } else {
                loadingOverlay.hide();
                Modal.showAlert("Số điện thoại hoặc email không đúng. Xin vui lòng kiểm tra lại.");
            }
        } else {
            loadingOverlay.hide();
            Modal.showAlert("Không thể kết nối đến cơ sở dữ liệu!");
        }
    }

    public void handleChangePassword(ActionEvent actionEvent) throws SQLException {
        loadingOverlay.show();
        String phone = fp_inputuser.getText();
        String newPassword = np_newPassword.getText();
        String confirmPassword = np_comfirmPassword.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            loadingOverlay.hide();
            Modal.showAlert("Xin vui lòng nhập đầy đủ mật khẩu mới và xác nhận mật khẩu!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            loadingOverlay.hide();
            Modal.showAlert("Mật khẩu xác nhận không trùng khớp!");
            return;
        }

        if (Config.isValidPassword(newPassword)) {
            loadingOverlay.hide();
            Modal.showAlert("Mật khẩu mới không hợp lệ!");
            return;
        }

        Connection connection = MySQLConnection.connect();
        String query = "UPDATE users SET password = ? WHERE phone = ?";
        if (connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Config.hashPassword(newPassword));
            preparedStatement.setString(2, phone);

            int rowsUpdated = preparedStatement.executeUpdate();
            loadingOverlay.hide();
            if (rowsUpdated > 0) {
                Modal.showAlert("Thay đổi mật khẩu thành công! Xin vui lòng đăng nhập lại.");
                np_newPassForm.setVisible(false);
                si_loginForm.setVisible(true);
            } else {
                Modal.showAlert("Không tìm thấy tài khoản với số điện thoại đã cung cấp!");
            }
        } else {
            loadingOverlay.hide();
            Modal.showAlert("Không thể kết nối đến cơ sở dữ liệu!");
        }
    }

    public void switchForgotPass() {
        fp_inputform.setVisible(true);
        si_loginForm.setVisible(false);
    }

    public void backToLoginForm() {
        fp_inputform.setVisible(false);
        si_loginForm.setVisible(true);

    }

    public void backToInputForm() {
        fp_inputform.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    public void switchForm(ActionEvent actionEvent) {

        TranslateTransition slider = new TranslateTransition();

        if (actionEvent.getSource() == side_CreateBtn) {
            slider.setNode(side_form);
            slider.setToX(350);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                fp_inputform.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });

            slider.play();
        } else if (actionEvent.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);
                fp_inputform.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });

            slider.play();
        }
    }


}

