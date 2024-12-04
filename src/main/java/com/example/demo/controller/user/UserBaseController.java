package com.example.demo.controller.user;
import java.io.IOException;
import com.example.demo.Utils.PreferencesUtils;
import static com.example.demo.config.button.ButtonHandler.handleNavigator;
import com.example.demo.config.loading.LoadingOverlay;
import com.example.demo.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class UserBaseController {

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
        User user = PreferencesUtils.getUser();
        assert user != null;
        welcomeLabel.setText(String.format("Xin chào, %s!", user.getName()));
        setMainContent("/com/example/demo/controller/auth/view/auth/dashboard/dashboard-view.fxml");
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
        setMainContent("/com/example/demo/controller/auth/view/auth/dashboard/dashboard-view.fxml");
    }

    @FXML
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        PreferencesUtils.clearUser();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", false);
    }

    public void imPortOnClick(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/admin/settings/setting-view.fxml");
    }

    public void onSales(ActionEvent actionEvent) {
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/view/auth/salesdashboardlayout/sales-dashboard-layout.fxml", false);
    }
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        setMainContent("/com/example/demo/controller/auth/view/auth/addproduct/addproduct.fxml");
    }
}
