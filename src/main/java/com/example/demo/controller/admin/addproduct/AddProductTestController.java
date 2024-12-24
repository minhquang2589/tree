package com.example.demo.controller.admin.addproduct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.demo.DAO.CategoryDAO;
import com.example.demo.DAO.ProductDAO;
import com.example.demo.DAO.SizeDAO;
import com.example.demo.DAO.VariantDAO;
import com.example.demo.Utils.Config;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.model.SizeQuantity;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Config.hashCodeSHA;
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


    public void handleAddImage(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        List<File> selectedFiles = (List<File>) Config.showFileChooser(currentStage);
        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                imageListView.getItems().add(String.valueOf(selectedFile));
                images.add(String.valueOf(selectedFile));
            }
        }
    }


    @FXML
    private void handleAddProduct() {
        System.out.println(sizeQuantities);
        String productName = nameField.getText();
        String priceText = priceField.getText();
        String description = descriptionField.getText();
        String selectedCategory = categoryField.getText();

        if (productName.isEmpty() || priceText.isEmpty() || description.isEmpty() || selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        try {
            Category selectedCategoryObj = categoryDAO.getCategoryByName(selectedCategory);
            if (selectedCategoryObj == null) {
                Category newCategory = new Category(0, selectedCategory, null, "Category description");
                selectedCategoryObj = categoryDAO.addCategory(newCategory);
            }

            int categoryId = selectedCategoryObj.getCategoryId();

            Product newProduct = new Product(productName, description, categoryId);

            productDAO.addProduct(newProduct);

            int productId = newProduct.getProductId();
            productDAO.addProductImages(productId, images);

            for (SizeQuantity sizeQuantity : sizeQuantities) {
                Size size = sizeQuantity.getSize();
                int quantity = sizeQuantity.getQuantity();
                Size existingSize = sizeDao.getSizeByName(size.getSize());
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
                String uniqueCode = hashCodeSHA(size.getSize() + quantity + timestamp);

                if (existingSize != null) {
                    variantDAO.addProductVariant(productId, existingSize.getSizeId(),999000, quantity, uniqueCode);
                } else {
                    int newSizeId = sizeDao.addSize(size);
                    if (newSizeId != -1) {
                        variantDAO.addProductVariant(productId, newSizeId, quantity,999000, uniqueCode);
                    } else {
                        showAlert("Error adding new size: " + size.getSize());
                        return;
                    }
                }
            }
            showAlert("Product added successfully!");
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @FXML
    private void handleAddSize() {
        HBox newSizeRow = new HBox(10);
        TextField newSizeInputField = new TextField();
        newSizeInputField.setPromptText("Size");
        newSizeInputField.setPrefWidth(150);

        TextField newQuantityInputField = new TextField();
        newQuantityInputField.setPromptText("Quantity");
        newQuantityInputField.setPrefWidth(150);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            sizesVBox.getChildren().remove(newSizeRow);
            sizeQuantities.removeIf(sizeQuantity -> sizeQuantity.getSize().getSize().equals(newSizeInputField.getText())); // Remove from sizeQuantities list
            updateSizeQuantitiesView();
        });

        newSizeRow.getChildren().addAll(newSizeInputField, newQuantityInputField, deleteButton);

        sizesVBox.getChildren().addFirst(newSizeRow);

        newQuantityInputField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String sizeText = newSizeInputField.getText();
                String quantityText = newQuantityInputField.getText();

                if (!sizeText.isEmpty() && !quantityText.isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(quantityText);
                        Size size = new Size(0, sizeText, "");
                        SizeQuantity sizeQuantity = new SizeQuantity(size, quantity);

                        sizeQuantities.add(sizeQuantity);
                        newSizeInputField.clear();
                        newQuantityInputField.clear();
                        updateSizeQuantitiesView();
                    } catch (NumberFormatException e) {
                        showAlert("Invalid quantity entered.");
                    }
                } else {
                    showAlert("Please enter both size and quantity.");
                }
            }
        });
    }

    private void updateSizeQuantitiesView() {
        sizesVBox.getChildren().clear();
        for (SizeQuantity sizeQuantity : sizeQuantities) {
            HBox sizeRow = new HBox(10);

            Label sizeLabel = new Label("Size: " + sizeQuantity.getSize().getSize());
            Label quantityLabel = new Label("Quantity: " + sizeQuantity.getQuantity());
            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(event -> {
                sizeQuantities.remove(sizeQuantity);
                updateSizeQuantitiesView();
            });

            sizeRow.getChildren().addAll(sizeLabel, quantityLabel, deleteButton);
            sizesVBox.getChildren().add(sizeRow);
        }
    }

}
