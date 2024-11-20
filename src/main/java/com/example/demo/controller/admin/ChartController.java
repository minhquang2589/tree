package com.example.demo.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart;

public class ChartController {

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

    public void initialize() {
        if (xAxis != null && yAxis != null && revenueChart != null) {
            xAxis.setLabel("Tháng");
            yAxis.setLabel("Doanh thu (VND)");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu trong năm");

            series.getData().add(new XYChart.Data<>("Tháng 1", 1200000));
            series.getData().add(new XYChart.Data<>("Tháng 2", 1500000));
            series.getData().add(new XYChart.Data<>("Tháng 3", 1800000));
            series.getData().add(new XYChart.Data<>("Tháng 4", 2000000));
            series.getData().add(new XYChart.Data<>("Tháng 5", 1700000));
            series.getData().add(new XYChart.Data<>("Tháng 6", 2500000));

            revenueChart.getData().add(series);
        }

        if (lineXAxis != null && lineYAxis != null && lineChart != null) {
            lineXAxis.setLabel("Tháng");
            lineYAxis.setLabel("Doanh thu (VND)");

            XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
            lineSeries.setName("Doanh thu hàng tháng");

            lineSeries.getData().add(new XYChart.Data<>("Tháng 1", 1000000));
            lineSeries.getData().add(new XYChart.Data<>("Tháng 2", 1300000));
            lineSeries.getData().add(new XYChart.Data<>("Tháng 3", 1600000));
            lineSeries.getData().add(new XYChart.Data<>("Tháng 4", 1900000));
            lineSeries.getData().add(new XYChart.Data<>("Tháng 5", 2100000));
            lineSeries.getData().add(new XYChart.Data<>("Tháng 6", 2400000));

            lineChart.getData().add(lineSeries);
        }

        if (pieChart != null) {
            PieChart.Data slice1 = new PieChart.Data("Doanh thu Tháng 1", 1200000);
            PieChart.Data slice2 = new PieChart.Data("Doanh thu Tháng 2", 1500000);
            PieChart.Data slice3 = new PieChart.Data("Doanh thu Tháng 3", 1800000);
            PieChart.Data slice4 = new PieChart.Data("Doanh thu Tháng 4", 2000000);
            PieChart.Data slice5 = new PieChart.Data("Doanh thu Tháng 5", 1700000);
            PieChart.Data slice6 = new PieChart.Data("Doanh thu Tháng 6", 2500000);
            pieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5, slice6);
        }

        if (pieChart2 != null) {
            PieChart.Data slice1 = new PieChart.Data("Doanh thu Tháng 1", 1200000);
            PieChart.Data slice2 = new PieChart.Data("Doanh thu Tháng 2", 1500000);
            PieChart.Data slice3 = new PieChart.Data("Doanh thu Tháng 3", 1800000);
            PieChart.Data slice4 = new PieChart.Data("Doanh thu Tháng 4", 2000000);
            PieChart.Data slice5 = new PieChart.Data("Doanh thu Tháng 5", 1700000);
            PieChart.Data slice6 = new PieChart.Data("Doanh thu Tháng 6", 2500000);
            pieChart2.getData().addAll(slice1, slice2, slice3, slice4, slice5, slice6);
        }
    }
}
