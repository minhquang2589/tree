package com.example.demo.controller;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.loading.LoadingOverlay;
import com.example.demo.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import static com.example.demo.config.button.ButtonHandler.handleNavigator;

public class BaseController {

    @FXML
    public Label welcomeLabel;
    @FXML
    public StackPane rootPaneDashboard;
    @FXML
    private AnchorPane mainContent;

    @FXML
    public void initialize() throws IOException {
        LoadingOverlay loadingOverlay = new LoadingOverlay();
        loadingOverlay.addTo(rootPaneDashboard);
        setMainContent("/com/example/demo/controller/auth/view/admin/dashboard/dashboard-view.fxml");
        User user = PreferencesUtils.getUser();
        assert user != null;
        welcomeLabel.setText(String.format("Xin chào, %s!", user.getName()));
    }


    public void setMainContent(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent content = loader.load();
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        mainContent.getChildren().clear();
        mainContent.getChildren().add(content);
    }


    @FXML
    public void onDashboardButtonClick(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/dashboard/dashboard-view.fxml");

    }

    @FXML
    public void onUsersButtonClick(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/account/account-view.fxml");

    }

    @FXML
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        PreferencesUtils.clearUser();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", false);
    }

    @FXML
    public void imPortOnClick(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/settings/setting-view.fxml");
    }

    @FXML
    public void onSales(ActionEvent actionEvent) {
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/view/user/salesdashboardlayout/sales-dashboard-layout.fxml", false);
    }

    @FXML
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/addproduct/addproduct.fxml");
    }

    @FXML
    public void onAddProductTest(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/addproduct/add-product-test-view.fxml");
    }

    @FXML
    public void voucherOnClick(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/voucher/voucher-view.fxml");
    }
}
