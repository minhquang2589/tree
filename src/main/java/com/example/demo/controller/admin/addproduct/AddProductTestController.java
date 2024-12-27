package com.example.demo.controller.admin.addproduct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.DAO.*;
import com.example.demo.Utils.Config;
import com.example.demo.model.*;
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
    public CheckBox isNewCheckBox;
    @FXML
    public TextField discountQuantityField;
    @FXML
    public TextField discountPercentageField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    private TextField nameField;
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
        String productName = nameField.getText();
        String description = descriptionField.getText();
        String selectedCategory = categoryField.getText();
        boolean isNew = isNewCheckBox.isSelected();
        String finalDiscountId = null;

        double discountPercentage = (discountPercentageField.getText() != null && !discountPercentageField.getText().isEmpty())
                ? Double.parseDouble(discountPercentageField.getText())
                : 0.0;
        int discountQuantity = (discountQuantityField.getText() != null && !discountQuantityField.getText().isEmpty()) ? Integer.parseInt(discountQuantityField.getText()) : 0;
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (productName.isEmpty() || description.isEmpty() || selectedCategory == null || selectedCategory.isEmpty()) {
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

            Product newProduct = new Product(productName, description, categoryId, isNew);
            productDAO.addProduct(newProduct);
            if (discountPercentage > 0 && discountQuantity > 0) {
                if (startDate == null || endDate == null) {
                    showAlert("Xin vui lòng chọn nhày bắt đầu và ngày kết thúc giảm giá cho sản phẩm này!");
                    return;
                } else if (!endDate.isAfter(startDate)) {
                    showAlert("Ngày kết thúc phải lớn hơn ngày bắt đầu. Vui lòng chọn lại!");
                    return;
                } else {
                    Discount newDiscount = new Discount(discountPercentage, discountQuantity, discountQuantity, startDate, endDate);
                    int discountId = DiscountDAO.addDiscount(newDiscount);
                    finalDiscountId = (discountId != -1) ? Integer.toString(discountId) : null;
                }
            }

            int productId = newProduct.getProductId();
            productDAO.addProductImages(productId, images);
            for (SizeQuantity sizeQuantity : sizeQuantities) {
                Size size = sizeQuantity.getSize();
                int quantity = sizeQuantity.getQuantity();
                int price = sizeQuantity.getPrice();
                Size existingSize = sizeDao.getSizeByName(size.getSize());
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
                String uniqueCode = hashCodeSHA(size.getSize() + quantity + timestamp);

                if (existingSize != null) {
                    variantDAO.addProductVariant(productId, existingSize.getSizeId(), price, quantity, uniqueCode, finalDiscountId);
                } else {
                    int newSizeId = sizeDao.addSize(size);
                    if (newSizeId != -1) {
                        variantDAO.addProductVariant(productId, newSizeId, price, quantity, uniqueCode, finalDiscountId);
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
        TextField newPriceInputField = new TextField();
        newQuantityInputField.setPromptText("Quantity");
        newQuantityInputField.setPrefWidth(150);
        newPriceInputField.setPromptText("Price");
        newPriceInputField.setPrefWidth(150);
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            sizesVBox.getChildren().remove(newSizeRow);
            sizeQuantities.removeIf(sizeQuantity ->
                    sizeQuantity.getSize().getSize().equals(newSizeInputField.getText()));
            updateSizeQuantitiesView();
        });

        newSizeRow.getChildren().addAll(newSizeInputField, newQuantityInputField, newPriceInputField, deleteButton);

        sizesVBox.getChildren().addFirst(newSizeRow);
        newQuantityInputField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                addSizeQuantity(newSizeInputField, newQuantityInputField, newPriceInputField);
            }
        });

        newPriceInputField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                addSizeQuantity(newSizeInputField, newQuantityInputField, newPriceInputField);
            }
        });
    }

    private void addSizeQuantity(TextField sizeField, TextField quantityField, TextField priceField) {
        String sizeText = sizeField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();

        if (!sizeText.isEmpty() && !quantityText.isEmpty() && !priceText.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityText);
                int price = Integer.parseInt(priceText);

                Size existingSize = sizeQuantities.stream()
                        .map(SizeQuantity::getSize)
                        .filter(size -> size.getSize().equals(sizeText))
                        .findFirst()
                        .orElse(null);

                if (existingSize != null) {
                    sizeQuantities.removeIf(sq -> sq.getSize().getSize().equals(sizeText));
                    SizeQuantity updatedSizeQuantity = new SizeQuantity(existingSize, quantity, price);
                    sizeQuantities.add(updatedSizeQuantity);
                } else {
                    Size newSize = new Size(0, sizeText, "");
                    sizeQuantities.add(new SizeQuantity(newSize, quantity, price));
                }

                sizeField.clear();
                quantityField.clear();
                priceField.clear();
                updateSizeQuantitiesView();
            } catch (NumberFormatException e) {
                showAlert("Invalid quantity or price entered.");
            }
        } else {
            showAlert("Please enter size, quantity, and price.");
        }
    }

    private void updateSizeQuantitiesView() {
        sizesVBox.getChildren().clear();
        for (SizeQuantity sizeQuantity : sizeQuantities) {
            HBox sizeRow = new HBox(10);
            Label sizeLabel = new Label("Size: " + sizeQuantity.getSize().getSize());
            Label quantityLabel = new Label("Quantity: " + sizeQuantity.getQuantity());
            Label priceLabel = new Label("Price: " + sizeQuantity.getPrice());
            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(event -> {
                sizeQuantities.remove(sizeQuantity);
                updateSizeQuantitiesView();
            });

            sizeRow.getChildren().addAll(sizeLabel, quantityLabel, priceLabel, deleteButton);
            sizesVBox.getChildren().add(sizeRow);
        }
    }


}
