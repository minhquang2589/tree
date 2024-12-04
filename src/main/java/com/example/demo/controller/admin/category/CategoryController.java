package com.example.demo.controller.admin.category;
import com.example.demo.Utils.Config;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Category;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.example.demo.Utils.Modal.*;

public class CategoryController {

    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TableColumn<Category, String> categoryColumn;
    @FXML
    private TableColumn<Category, String> imageColumn;
    @FXML
    private TableColumn<Category, String> descriptionColumn;
    @FXML
    private ImageView imageView;
    @FXML
    public TextField categoryField;
    @FXML
    public String imageField;
    @FXML
    public TextArea descriptionField;

    public void initialize() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageColumn.setCellFactory(new Callback<TableColumn<Category, String>, TableCell<Category, String>>() {
            @Override
            public TableCell<Category, String> call(TableColumn<Category, String> param) {
                return new TableCell<Category, String>() {
                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (empty || imageUrl == null || imageUrl.isEmpty()) {
                            setGraphic(null);
                        } else {
                            ImageView imageView = new ImageView();
                            imageView.setImage(new Image("file:" + imageUrl));
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadData();
    }

    private void loadData() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        Connection connection = MySQLConnection.connect();
            try {
                assert connection != null;
                try (Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        String category = resultSet.getString("category");
                        String imagePath = resultSet.getString("image");
                        String description = resultSet.getString("description");
                        categories.add(new Category(category, imagePath, description));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        categoriesTable.getItems().setAll(categories);
    }



    @FXML
    private void openViewUpload(ActionEvent event) {
        try {
            showModal("/com/example/demo/controller/auth/view/admin/category/upload-view.fxml", "Tải lên");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    @FXML
    private void handleSubmit() throws SQLException {
        String category = categoryField.getText();
        String image = imageField;
        String description = descriptionField.getText();

        if (category.isEmpty() || image.isEmpty() || description.isEmpty()) {
            showAlert("Vui lòng nhập đủ thông tin ");
            return;
        }
        Category newCategory = new Category(category, image, description);
        if (insertCategory(newCategory)) {
            showAlert("Tải lên thành công!", ()->{
                loadData();
                closeModal();
            });
        } else {
            showAlert("Lỗi. Vui lòng thử lại sau.");
        }
    }


    private boolean insertCategory(Category category) throws SQLException {
        String query = "INSERT INTO categories (category, image, description) VALUES (?, ?, ?)";
        Connection connection = MySQLConnection.connect();

        if (connection != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, category.getImage());
            preparedStatement.setString(3, category.getDescription());
            preparedStatement.executeUpdate();
            return true;
        }
        return false;
    }

    public void handleBrowse(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File selectedFile = Config.showFileChooser(stage);
        if (selectedFile != null) {
            String savedImagePath = Config.saveImage(selectedFile.getName(), selectedFile);
            if (savedImagePath != null) {
                imageField = savedImagePath;
                Image image = new Image("file:" + savedImagePath);
                imageView.setImage(image);
            } else {
                showAlert(null);
            }
        }
    }
}
