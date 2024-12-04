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
import javafx.scene.layout.HBox;
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
            TextField nameField = new TextField(selectedUser.getName());
            TextField emailField = new TextField(selectedUser.getEmail());
            TextField phoneField = new TextField(selectedUser.getPhone());
            ComboBox<String> genderComboBox = new ComboBox<>();
            ComboBox<String> roleComboBox = new ComboBox<>();
            genderComboBox.getItems().addAll("male", "female", "other");
            roleComboBox.getItems().addAll("user", "admin", "moderator", "supervisor");
            genderComboBox.setValue(selectedUser.getGender());
            roleComboBox.setValue(selectedUser.getRole());

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

            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(
                    new Label("Họ và tên:"), nameField,
                    new Label("Email:"), emailField,
                    new Label("Số điện thoại:"), phoneField,
                    new Label("Giới tính:"), genderComboBox,
                    new Label("Role:"), roleComboBox,
                    chooseImageButton, imageView
            );

            Button saveButton = new Button("Lưu");
            saveButton.setOnAction(event -> {
                boolean checkPhone = Config.isPhoneNumberValid(phoneField.getText());
                boolean checkEmail = Config.isEmailValid(emailField.getText());
                if (!checkPhone && checkEmail) {
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

            Button cancelButton = new Button("Huỷ");
            cancelButton.setOnAction(event -> {
                ((Stage) cancelButton.getScene().getWindow()).close();
            });

            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(saveButton, cancelButton);
            vbox.getChildren().add(buttonBox);

            Scene modalScene = new Scene(vbox, 700, 600);
            Stage modalStage = new Stage();
            modalStage.setTitle("Sửa thông tin");
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
