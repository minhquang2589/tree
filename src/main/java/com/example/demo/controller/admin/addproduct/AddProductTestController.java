package com.example.demo.controller.admin.addproduct;

import com.example.demo.DAO.CategoryDAO;
import com.example.demo.DAO.ProductDAO;
import com.example.demo.DAO.SizeDAO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Size;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddProductTestController {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ListView<String> imageListView;
    @FXML private TableView<Size> sizesTableView;
    @FXML private TableColumn<Size, String> sizeColumn;
    @FXML private TableColumn<Size, Integer> quantityColumn;

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private SizeDAO sizeDAO = new SizeDAO();

    private List<String> images = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            categoryComboBox.getItems().addAll(categoryDAO.getAllCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
//        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                imageListView.getItems().add(file.getAbsolutePath());
                images.add(file.getAbsolutePath());
            }
        }
    }

    @FXML
    private void handleAddProduct() {
        String productName = nameField.getText();
        String priceText = priceField.getText();
        String description = descriptionField.getText();
        Category selectedCategory = categoryComboBox.getValue();

        if (productName.isEmpty() || priceText.isEmpty() || description.isEmpty() || selectedCategory == null) {
            showAlert("All fields must be filled!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            Product newProduct = new Product(productName, price, description, selectedCategory.getCategoryId());

            productDAO.addProduct(newProduct);

            productDAO.addProductImages(newProduct.getProductId(), images);

            for (Size size : sizes) {
                productDAO.addProductSize(newProduct.getProductId(), size);
            }

            showAlert("Product added successfully!");
        } catch (NumberFormatException | SQLException e) {
            showAlert("Error adding product: " + e.getMessage());
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleAddSize() {
        TextInputDialog sizeDialog = new TextInputDialog();
        sizeDialog.setTitle("Add Size");
        sizeDialog.setHeaderText("Enter size (e.g. S, M, L):");
        sizeDialog.showAndWait().ifPresent(sizeText -> {
            TextInputDialog quantityDialog = new TextInputDialog("1");  // Set default quantity as 1
            quantityDialog.setTitle("Enter Quantity");
            quantityDialog.setHeaderText("Enter quantity for " + sizeText + ":");
            quantityDialog.showAndWait().ifPresent(quantityText -> {
//                try {
//                    int quantity = Integer.parseInt(quantityText);
//                    Size size = new Size(sizeText, quantity);
//                    sizes.add(size);
//                    sizesTableView.getItems().add(size);
//                } catch (NumberFormatException e) {
//                    showAlert("Invalid quantity!");
//                }
            });
        });
    }

}
