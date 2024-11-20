package com.example.demo.controller.admin;
import com.example.demo.DAO.StoreDAO;
import com.example.demo.Utils.Modal;
import com.example.demo.model.StoreModel;
import com.example.demo.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;

public class StoreViewController {

    @FXML
    private TableColumn actionColumn;
    @FXML
    private TableColumn<StoreModel, String> nameColumn;
    @FXML
    private TableView<StoreModel> storeTable;
    @FXML

    private TableColumn<StoreModel, String> phoneColumn;
    @FXML
    private TableColumn<StoreModel, String> addressColumn;
    @FXML
    private TableColumn<StoreModel, String> imageColumn;
    @FXML
    private TableColumn<StoreModel, String> statusColumn;
    @FXML
    private TableColumn<StoreModel, String> startColumn;
    @FXML
    private TableColumn<StoreModel, String> endColumn;

    private final ObservableList<StoreModel> StoreList = FXCollections.observableArrayList();


    @FXML
    public void initialize() throws SQLException {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        imageColumn.setCellFactory(new Callback<TableColumn<StoreModel, String>, TableCell<StoreModel, String>>() {
            @Override
            public TableCell<StoreModel, String> call(TableColumn<StoreModel, String> param) {
                return new TableCell<StoreModel, String>() {
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


        actionColumn.setCellFactory(new Callback<TableColumn<StoreModel, String>, TableCell<StoreModel, String>>() {
            @Override
            public TableCell<StoreModel, String> call(TableColumn<StoreModel, String> param) {
                return new TableCell<StoreModel, String>() {
                    private final Button deleteButton = new Button("Xoá");
                    private final Button editButton = new Button("Sửa");

                    {
                        deleteButton.getStyleClass().add("delete-button");
                        editButton.getStyleClass().add("edit-button");
                        deleteButton.setOnAction(event -> handleDeleteStore(getTableRow().getItem()));
                        editButton.setOnAction(event -> handleEditStore(getTableRow().getItem()));
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
        loadData();
    }

    private void loadData() throws SQLException {
        StoreDAO storeDAO = new StoreDAO();
        StoreList.setAll(storeDAO.getAllStore());
        storeTable.setItems(StoreList);
    }


    private void handleEditStore(StoreModel item) {
    }

    private void handleDeleteStore(StoreModel item) {
    }


    @FXML
    public void uploadButtonOnClick(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/components/modal/store-upload-modal.fxml", "Thêm cửa hàng");
    }


}
