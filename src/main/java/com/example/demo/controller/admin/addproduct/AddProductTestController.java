package com.example.demo.controller.admin.addproduct;

import com.example.demo.DAO.CategoryDAO;
import com.example.demo.DAO.ProductDAO;
import com.example.demo.DAO.SizeDAO;
import com.example.demo.DAO.VariantDAO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.model.SizeQuantity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Modal.showAlert;

public class AddProductTestController {

    public TextField categoryField;

    private final List<SizeQuantity> sizeQuantities = new ArrayList<>();
    @FXML
    public VBox sizesVBox;

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ListView<String> imageListView;
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final SizeDAO sizeDao = new SizeDAO();
    private final VariantDAO variantDAO = new VariantDAO();

    private final List<String> images = new ArrayList<>();
    private final List<Size> sizes = new ArrayList<>();

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

        if (productName.isEmpty() || priceText.isEmpty() || description.isEmpty() || selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            Category selectedCategoryObj = categoryDAO.getCategoryByName(selectedCategory);
            if (selectedCategoryObj == null) {
                Category newCategory = new Category(0, selectedCategory, null, "Category description");
                selectedCategoryObj = categoryDAO.addCategory(newCategory);
            }

            int categoryId = selectedCategoryObj.getCategoryId();

            Product newProduct = new Product(productName, price, description, categoryId);

            productDAO.addProduct(newProduct);

            int productId = newProduct.getProductId();

            productDAO.addProductImages(productId, images);

            for (SizeQuantity sizeQuantity : sizeQuantities) {
                System.out.println(sizeQuantities);
                Size size = sizeQuantity.getSize();
                int sizeId = sizeDao.addSize(size);

                if (sizeId != -1) {
                    int quantity = sizeQuantity.getQuantity();
                    variantDAO.addProductVariant(productId, sizeId, quantity);
                }
            }


            showAlert("Product added successfully!");
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleAddSize() {
        System.out.println(sizeQuantities);
        HBox newSizeRow = new HBox(10);
        TextField newSizeInputField = new TextField();
        newSizeInputField.setPromptText("Size");
        newSizeInputField.setPrefWidth(150);
        TextField newQuantityInputField = new TextField();
        newQuantityInputField.setPromptText("Quantity");
        newQuantityInputField.setPrefWidth(150);
        newSizeRow.getChildren().addAll(newSizeInputField, newQuantityInputField);
        sizesVBox.getChildren().add(newSizeRow);
        newSizeInputField.setOnAction(event -> {
            String sizeText = newSizeInputField.getText();
            String quantityText = newQuantityInputField.getText();

            if (!sizeText.isEmpty() && !quantityText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);

                    Size existingSize = sizeDao.getSizeByName(sizeText);
                    Size size;
                    if (existingSize != null) {
                        size = existingSize;
                    } else {
                        size = new Size(0, sizeText, "");
                        int sizeId = sizeDao.addSize(size);
                        if (sizeId != -1) {
                            size.setSizeId(sizeId);
                        } else {
                            showAlert("Failed to add size.");
                            return;
                        }
                    }

                    SizeQuantity sizeQuantity = new SizeQuantity(size, quantity);
                    sizeQuantities.add(sizeQuantity);
                    System.out.println("Added sizeQuantity: " + sizeQuantity);
                    System.out.println("Current sizeQuantities list: " + sizeQuantities);

                    newSizeInputField.clear();
                    newQuantityInputField.clear();

                } catch (NumberFormatException e) {
                    showAlert("Invalid quantity entered.");
                } catch (SQLException e) {
                    showAlert("Database error: " + e.getMessage());
                }
            } else {
                showAlert("Please enter both size and quantity.");
            }
        });

    }


}
