package com.example.demo.controller.admin.addproduct;

import com.example.demo.DAO.CategoryDAO;
import com.example.demo.DAO.ProductDAO;
import com.example.demo.DAO.SizeDAO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Size;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Modal.showAlert;

public class AddProductTestController {

    public TextField categoryField;
    public TextField sizeInputField;
    public TextField quantityInputField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionField;
    @FXML private ListView<String> imageListView;
    @FXML private TableView<Size> sizesTableView;
    @FXML private TableColumn<Size, String> sizeColumn;
    @FXML private TableColumn<Size, Integer> quantityColumn;

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private SizeDAO sizeDAO = new SizeDAO();

    private final List<String> images = new ArrayList<>();
    private final List<Size> sizes = new ArrayList<>();

    @FXML
    public void initialize() {
        sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSize()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
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
        String selectedCategory = categoryField.getText();

        if (productName.isEmpty() || priceText.isEmpty() || description.isEmpty() || selectedCategory == null) {
            showAlert("All fields must be filled!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            Product newProduct = new Product(productName, price, description, 1);

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



    @FXML
    private void handleAddSize() {
        String sizeText = sizeInputField.getText();
        String quantityText = quantityInputField.getText();
        String descriptionText = descriptionField.getText();

        if (sizeText.isEmpty() || quantityText.isEmpty() || descriptionText.isEmpty()) {
            showAlert("Size, quantity, and description must be filled!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            Size size = new Size(0, sizeText, quantity, descriptionText);
            sizesTableView.getItems().add(size);
            sizes.add(size);
            sizeInputField.clear();
            quantityInputField.clear();
            descriptionField.clear();

        } catch (NumberFormatException e) {
            showAlert("Invalid quantity!");
        }
    }



    public SizeDAO getSizeDAO() {
        return sizeDAO;
    }

    public void setSizeDAO(SizeDAO sizeDAO) {
        this.sizeDAO = sizeDAO;
    }
}
