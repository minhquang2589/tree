package com.example.demo.controller.admin;

import com.example.demo.DAO.UserDAO;
import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class UserController {

    @FXML
    private TableColumn<UserModel, String> phoneColumn;
    @FXML
    private TableColumn<UserModel, String> addressColumn;
    @FXML
    private TableColumn<UserModel, String> genderColumn;
    @FXML
    private TableColumn<UserModel, String> birthdayColumn;
    @FXML
    private TableColumn<UserModel, String> imageColumn;
    @FXML
    private TableView<UserModel> userTable;
    @FXML
    private TableColumn<UserModel, String> usernameColumn;
    @FXML
    private TableColumn<UserModel, String> emailColumn;
    @FXML
    private TableColumn<UserModel, String> roleColumn;
    @FXML
    private TableColumn<UserModel, String> actionColumn;

    private final ObservableList<UserModel> userList = FXCollections.observableArrayList();

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

        imageColumn.setCellFactory(new Callback<TableColumn<UserModel, String>, TableCell<UserModel, String>>() {
            @Override
            public TableCell<UserModel, String> call(TableColumn<UserModel, String> param) {
                return new TableCell<UserModel, String>() {
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

        actionColumn.setCellFactory(new Callback<TableColumn<UserModel, String>, TableCell<UserModel, String>>() {
            @Override
            public TableCell<UserModel, String> call(TableColumn<UserModel, String> param) {
                return new TableCell<UserModel, String>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button editButton = new Button("Edit");

                    {
                        deleteButton.setOnAction(event -> handleDeleteUser(getTableRow().getItem()));
                        editButton.setOnAction(event -> handleEditUser(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(30);
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
        userList.setAll(userDAO.getAllUsers());
        userTable.setItems(userList);
    }

    public void handleEditUser(UserModel selectedUser) {
        if (selectedUser != null) {
            TextField nameField = new TextField(selectedUser.getName());
            TextField emailField = new TextField(selectedUser.getEmail());
            TextField phoneField = new TextField(selectedUser.getPhone());
            TextField addressField = new TextField(selectedUser.getAddress());
            ComboBox<String> genderComboBox = new ComboBox<>();
            ComboBox<String> roleComboBox = new ComboBox<>();
            genderComboBox.getItems().addAll("Male", "Female", "none");
            roleComboBox.getItems().addAll("user", "admin", "none");
            genderComboBox.setValue(selectedUser.getGender());
            roleComboBox.setValue(selectedUser.getRole());

            ImageView imageView = new ImageView();
            if (selectedUser.getImage() != null) {
                imageView.setImage(new Image("file:" + selectedUser.getImage()));
            }
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            Button chooseImageButton = new Button("Choose Image");
            chooseImageButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                Stage stage = (Stage) chooseImageButton.getScene().getWindow();
                java.io.File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    Path targetDir = Paths.get("assets/images");
                    if (!Files.exists(targetDir)) {
                        try {
                            Files.createDirectories(targetDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    Path targetPath = targetDir.resolve(file.getName());
                    try {
                        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                        selectedUser.setImage(targetPath.toString());
                        imageView.setImage(new Image("file:" + targetPath.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(
                    new Label("Name:"), nameField,
                    new Label("Email:"), emailField,
                    new Label("Phone:"), phoneField,
                    new Label("Address:"), addressField,
                    new Label("Gender:"), genderComboBox,
                    new Label("Role:"), roleComboBox,
                    chooseImageButton, imageView
            );

            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                boolean checkPhone = Config.isPhoneNumberValid(phoneField.getText());
                boolean checkEmail = Config.isEmailValid(emailField.getText());
                if (!checkPhone && checkEmail) {
                    selectedUser.setName(nameField.getText());
                    selectedUser.setEmail(emailField.getText());
                    selectedUser.setPhone(phoneField.getText());
                    selectedUser.setAddress(addressField.getText());
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
                    Modal.showAlert("Thất bại", "Xin vui lòng nhập đúng thông tin và thử lại sau!", null, null, null);
                }
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(event -> {
                ((Stage) cancelButton.getScene().getWindow()).close();
            });

            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(saveButton, cancelButton);
            vbox.getChildren().add(buttonBox);

            Scene modalScene = new Scene(vbox, 700, 600);
            Stage modalStage = new Stage();
            modalStage.setTitle("Edit User");
            modalStage.setScene(modalScene);
            modalStage.show();
        }
    }

    public void handleDeleteUser(UserModel selectedUser) {
        if (selectedUser != null) {
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
                            Modal.showAlert("Thất bại", "Xoá người dùng không thành cônh. vui lòng thử lại sai", null, null, null);
                        }
                    },
                    null
            );
        }
    }


}
