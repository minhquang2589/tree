package com.example.demo.controller;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.loading.LoadingOverlay;
import com.example.demo.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;

public class BaseController {

    @FXML
    public Label welcomeLabel;
    public StackPane rootPaneDashboard;
    @FXML
    private Button dashboardMenuItem;
    @FXML
    private Button userMenuItem;
    @FXML
    private Button settingsMenuItem;
    @FXML
    private Button logoutMenuItem;
    @FXML
    private AnchorPane mainContent;

    @FXML
    public void initialize() throws IOException {
        LoadingOverlay loadingOverlay = new LoadingOverlay();
        loadingOverlay.addTo(rootPaneDashboard);
        setMainContent("/com/example/demo/controller/auth/view/admin/dashboard/dashboard-view.fxml");
        UserModel user = PreferencesUtils.getUser();
        assert user != null;
        welcomeLabel.setText(String.format("Welcome, %s!", user.getName()));

        if (dashboardMenuItem != null) {
            dashboardMenuItem.setOnAction(event -> {
                try {
                    setMainContent("/com/example/demo/controller/auth/view/admin/dashboard/dashboard-view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }

        if (userMenuItem != null) {
            userMenuItem.setOnAction(event -> {
                try {
                    setMainContent("/com/example/demo/controller/auth/view/admin/account/account-view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }

        if (settingsMenuItem != null) {
            settingsMenuItem.setOnAction(event -> {
                try {
                    setMainContent("/com/example/demo/controller/auth/view/admin/settings/setting-view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (logoutMenuItem != null) {
            logoutMenuItem.setOnAction(event -> {
                PreferencesUtils.clearAll();
                handleNavigator(event, "/com/example/demo/controller/auth/login-view.fxml", false);
            });
        }
    }


    public void setMainContent(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent content = loader.load();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(content);
    }

    @FXML
    public void onDashboardButtonClick(ActionEvent actionEvent) throws IOException {
    }

    @FXML
    public void onUsersButtonClick(ActionEvent actionEvent) throws IOException {
    }

    @FXML
    public void onSettingsButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        PreferencesUtils.clearAll();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", false);
    }
}
