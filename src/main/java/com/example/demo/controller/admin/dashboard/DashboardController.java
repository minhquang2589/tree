package com.example.demo.controller.admin.dashboard;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.OrderItem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.demo.Utils.Config.formatCurrencyVND;

public class DashboardController {


    @FXML
    private BarChart<String, Number> revenueChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis lineXAxis;

    @FXML
    private NumberAxis lineYAxis;

    @FXML
    private PieChart pieChart;

    @FXML
    private PieChart pieChart2;
    @FXML
    private TableView<OrderItem> orderTable;

    @FXML
    private TableColumn<OrderItem, String> productNameColumn;

    @FXML
    private TableColumn<OrderItem, String> orderCodeColumn;

    @FXML
    private TableColumn<OrderItem, String> status;

    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderItem, String> orderDateColumn;
    @FXML
    private TableColumn<OrderItem, String> productImageColumn;

    @FXML
    private TableColumn<OrderItem, String> discountColumn;
    @FXML
    private TableColumn<OrderItem, String> totalPriceColumn;
    @FXML
    private TableColumn<OrderItem, String> sizeColumn;
    @FXML
    private TableColumn actionColumn;


    public void initialize() {
        Connection connection = MySQLConnection.connect();

        if (connection != null) {
            loadRevenueData(connection);
            loadMonthlyRevenueData(connection);
            loadPieChartData(connection, pieChart);
            loadPieChartData(connection, pieChart2);
            loadOrderData(connection);
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
            orderCodeColumn.setCellValueFactory(new PropertyValueFactory<>("order_reference"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            sizeColumn.setCellValueFactory(new PropertyValueFactory<>("productSize"));
            orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
            discountColumn.setCellValueFactory(new PropertyValueFactory<>("order_discount"));
            discountColumn.setCellFactory(col -> new TableCell<OrderItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatCurrencyVND(Double.parseDouble(item)));
                    }
                }
            });
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("order_total_price"));
            totalPriceColumn.setCellFactory(col -> new TableCell<OrderItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatCurrencyVND(Double.parseDouble(item)));
                    }
                }
            });
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            productImageColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getImage()));
            productImageColumn.setCellFactory(col -> new TableCell<OrderItem, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        javafx.scene.image.Image image = new Image("file:" + item);
                        imageView.setImage(image);
                        imageView.setFitHeight(120);
                        imageView.setFitWidth(180);
                        imageView.setPreserveRatio(true);
                        imageView.setStyle("-fx-border-color: gray; -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 1px; -fx-effect: innershadow( gaussian , rgba(0,0,0,0.1), 10, 0, 0, 0 );");
                        setGraphic(imageView);
                        setGraphic(imageView);
                    }
                }
            });

        }
    }


    private void loadRevenueData(Connection connection) {
        if (xAxis != null && yAxis != null && revenueChart != null) {
            xAxis.setLabel("Tháng");
            yAxis.setLabel("Doanh thu (VND)");


            XYChart.Series<String, Number> series = new XYChart.Series<>();

            String query = "SELECT MONTH(order_date) AS month, SUM(total_price) AS revenue FROM orders WHERE YEAR(order_date) = YEAR(CURDATE()) GROUP BY month";

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String month = "Tháng " + rs.getInt("month");
                    double revenue = rs.getDouble("revenue");
                    series.getData().add(new XYChart.Data<>(month, revenue));
                }

                revenueChart.getData().add(series);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMonthlyRevenueData(Connection connection) {
        if (lineXAxis != null && lineYAxis != null && lineChart != null) {
            lineXAxis.setLabel("Tháng");
            lineYAxis.setLabel("Doanh thu (VND)");

            XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
            lineSeries.setName("Doanh thu hàng tháng");

            String query = "SELECT MONTH(order_date) AS month, SUM(total_price) AS revenue FROM orders WHERE YEAR(order_date) = YEAR(CURDATE()) GROUP BY month";

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String month = "Tháng " + rs.getInt("month");
                    double revenue = rs.getDouble("revenue");
                    lineSeries.getData().add(new XYChart.Data<>(month, revenue));
                }

                lineChart.getData().add(lineSeries);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPieChartData(Connection connection, PieChart pieChart) {
        if (pieChart != null) {
            String query = "SELECT MONTH(order_date) AS month, SUM(total_price) AS revenue FROM orders WHERE YEAR(order_date) = YEAR(CURDATE()) GROUP BY month";

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String month = "Tháng " + rs.getInt("month");
                    double revenue = rs.getDouble("revenue");
                    PieChart.Data slice = new PieChart.Data(month, revenue);
                    pieChart.getData().add(slice);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadOrderData(Connection connection) {
        if (orderTable != null) {
            String query = """
                SELECT o.order_id,
                       o.status,
                       v.variant_id,
                       p.name AS product_name,
                       v.code AS product_code,
                       o.order_reference,
                       oi.quantity,
                       o.discount AS order_discount,
                       o.total_price AS order_total_price,
                       o.order_date,
                       i.image AS product_image,
                       s.size AS productSize,
                       c.category AS product_category
                FROM orders o
                JOIN order_items oi ON o.order_id = oi.order_id
                JOIN variants v ON oi.variant_id = v.variant_id
                JOIN products p ON v.product_id = p.product_id
                LEFT JOIN images i ON p.product_id = i.product_id
                LEFT JOIN sizes s ON v.size_id = s.size_id
                LEFT JOIN categories c ON p.category_id = c.category_id
                ORDER BY o.order_date DESC
            """;


            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();

                while (rs.next()) {
                    Integer orderId = rs.getInt("order_id");
                    Integer variantId = rs.getInt("variant_id");
                    String productName = rs.getString("product_name");
                    String productCode = rs.getString("product_code");
                    Integer quantity = rs.getInt("quantity");
                    String orderDate = rs.getString("order_date");
                    String productImage = rs.getString("product_image");
                    String status = rs.getString("status");
                    String order_reference = rs.getString("order_reference");
                    String order_discount = rs.getString("order_discount");
                    String order_total_price = rs.getString("order_total_price");
                    String productSize = rs.getString("productSize");
                    String product_category = rs.getString("product_category");
                    orderItems.add(new OrderItem(orderId, variantId, productName, productCode, quantity, orderDate, productImage, status, order_reference,order_discount,order_total_price,productSize,product_category));
                }

                orderTable.setItems(orderItems);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

