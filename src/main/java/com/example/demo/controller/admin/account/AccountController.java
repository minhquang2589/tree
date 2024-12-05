package com.example.demo.controller.admin.account;

import com.example.demo.DAO.UserDAO;
import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

public class AccountController {

    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, String> addressColumn;
    @FXML
    private TableColumn<User, String> genderColumn;
    @FXML
    private TableColumn<User, String> birthdayColumn;
    @FXML
    private TableColumn<User, String> imageColumn;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> actionColumn;

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    User user = PreferencesUtils.getUser();

    @FXML
    public void initialize() throws SQLException {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        imageColumn.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView imageView = new ImageView();
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                imageView.setImage(new Image("file:" + imageUrl));
                            }
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });


        actionColumn.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    private final Button deleteButton = new Button("Xoá");
                    private final Button editButton = new Button("Sửa");

                    {
                        deleteButton.getStyleClass().add("delete-button");
                        editButton.getStyleClass().add("edit-button");
                        deleteButton.setOnAction(event -> handleDeleteUser(getTableRow().getItem()));
                        editButton.setOnAction(event -> handleEditUser(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(3);
                            hBox.setAlignment(Pos.CENTER);
                            editButton.setMaxWidth(Double.MAX_VALUE);
                            deleteButton.setMaxWidth(Double.MAX_VALUE);
                            hBox.getChildren().addAll(editButton, deleteButton);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });

        loadUserData();
    }


    private void loadUserData() throws SQLException {
        UserDAO userDAO = new UserDAO();
        userList.setAll(userDAO.getUsers());
        userTable.setItems(userList);
    }

    public void handleEditUser(User selectedUser) {
        if (selectedUser != null) {
            VBox vbox = new VBox(10);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPrefWidth(850);
            vbox.setPrefHeight(500);

            Label headingLabel = new Label("Sửa Thông Tin Người Dùng");
            headingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
            vbox.getChildren().add(headingLabel);
            GridPane gridPane = new GridPane();
            gridPane.setHgap(15);
            gridPane.setVgap(15);
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setStyle("-fx-background-color: #f7f7f7; -fx-padding: 20;");

            Label nameLabel = new Label("Họ và tên:");
            nameLabel.getStyleClass().add("q-label");
            TextField nameField = new TextField(selectedUser.getName());
            nameField.setPromptText("Nhập họ và tên");
            nameField.getStyleClass().add("q-form");

            Label emailLabel = new Label("Email:");
            emailLabel.getStyleClass().add("q-label");
            TextField emailField = new TextField(selectedUser.getEmail());
            emailField.setPromptText("Nhập email");
            emailField.getStyleClass().add("q-form");

            Label phoneLabel = new Label("Số điện thoại:");
            phoneLabel.getStyleClass().add("q-label");
            TextField phoneField = new TextField(selectedUser.getPhone());
            phoneField.setPromptText("Nhập số điện thoại");
            phoneField.getStyleClass().add("q-form");

            Label genderLabel = new Label("Giới tính:");
            genderLabel.getStyleClass().add("q-label");
            ComboBox<String> genderComboBox = new ComboBox<>();
            genderComboBox.getItems().addAll("male", "female", "other");
            genderComboBox.setValue(selectedUser.getGender());
            genderComboBox.getStyleClass().add("q-form");

            Label roleLabel = new Label("Role:");
            roleLabel.getStyleClass().add("q-label");
            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.getItems().addAll("user", "admin", "moderator", "supervisor");
            roleComboBox.setValue(selectedUser.getRole());
            roleComboBox.getStyleClass().add("q-form");

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameField, 1, 0);
            gridPane.add(emailLabel, 0, 1);
            gridPane.add(emailField, 1, 1);
            gridPane.add(phoneLabel, 0, 2);
            gridPane.add(phoneField, 1, 2);
            gridPane.add(genderLabel, 0, 3);
            gridPane.add(genderComboBox, 1, 3);
            gridPane.add(roleLabel, 0, 4);
            gridPane.add(roleComboBox, 1, 4);

            ImageView imageView = new ImageView();
            if (selectedUser.getImage() != null) {
                imageView.setImage(new Image("file:" + selectedUser.getImage()));
            }
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            Button chooseImageButton = new Button("Chọn ảnh");
            chooseImageButton.setOnAction(event -> {
                File selectedFile = Config.showFileChooser((Stage) chooseImageButton.getScene().getWindow());
                if (selectedFile != null) {
                    String savedImagePath = Config.saveImage(selectedFile.getName(), selectedFile);
                    if (savedImagePath != null) {
                        selectedUser.setImage(savedImagePath);
                        imageView.setImage(new Image("file:" + savedImagePath));
                    } else {
                        Modal.showAlert(null);
                    }
                }
            });

            gridPane.add(chooseImageButton, 0, 5);
            gridPane.add(imageView, 1, 5);

            vbox.getChildren().add(gridPane);

            HBox buttonBox = new HBox(20);
            buttonBox.setAlignment(Pos.CENTER);
            Button saveButton = new Button("Lưu");
            saveButton.getStyleClass().add("save-button");
            saveButton.setOnAction(event -> {
                boolean checkPhone = Config.isPhoneNumberValid(phoneField.getText());
                boolean checkEmail = Config.isEmailValid(emailField.getText());
                if (checkPhone && checkEmail) {
                    selectedUser.setName(nameField.getText());
                    selectedUser.setEmail(emailField.getText());
                    selectedUser.setPhone(phoneField.getText());
                    selectedUser.setGender(genderComboBox.getValue());
                    selectedUser.setRole(roleComboBox.getValue());
                    try {
                        UserDAO userDAO = new UserDAO();
                        userDAO.updateUser(selectedUser);
                        loadUserData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ((Stage) saveButton.getScene().getWindow()).close();
                } else {
                    Modal.showAlert("Xin vui lòng nhập đúng thông tin và thử lại sau!");
                }
            });

            vbox.getChildren().add(buttonBox);

            StackPane rootPane = new StackPane(vbox);
            StackPane.setAlignment(vbox, Pos.CENTER);
            Scene modalScene = new Scene(rootPane, 850, 500);
            Stage modalStage = new Stage();
            modalScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/demo/styleCss/customStyle.css")).toExternalForm());
            modalStage.setTitle("Sửa Thông Tin Người Dùng");
            modalStage.setScene(modalScene);
            modalStage.show();
        }
    }


    public void handleDeleteUser(User selectedUser) {
        if (!Objects.equals(user.getRole(), "admin")) {
            Modal.showAlert("Bạn không có quyền xoá dữ liệu này");
        } else {
            if (user.getId() == selectedUser.getId()) {
                Modal.showAlert(null);
            } else {
                Modal.showAlert(
                        null,
                        "Bạn có muốn xoá user " + selectedUser.getName() + " không?",
                        Alert.AlertType.CONFIRMATION,
                        () -> {
                            try {
                                UserDAO userDAO = new UserDAO();
                                userDAO.deleteUser(selectedUser.getId());
                                loadUserData();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                Modal.showAlert("Xoá người dùng không thành cônh. vui lòng thử lại sai");
                            }
                        },
                        null
                );
            }

        }

    }


}
