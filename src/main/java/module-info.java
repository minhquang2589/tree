module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.google.zxing;
    requires javafx.swing;
    requires com.google.zxing.javase;
    requires java.prefs;
    requires java.net.http;
    requires java.persistence;
    requires itextpdf;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    opens com.example.demo.controller.auth to javafx.fxml;
    opens com.example.demo.config.button to javafx.fxml;
    opens com.example.demo.config.loading to javafx.fxml;
    opens com.example.demo.config;
    exports com.example.demo.config;
    exports com.example.demo.controller;
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.model to javafx.base;
    exports com.example.demo.controller.user;
    opens com.example.demo.controller.user to javafx.fxml;
    exports com.example.demo.controller.user.paymentprocessing;
    opens com.example.demo.controller.user.paymentprocessing to javafx.fxml;
    exports com.example.demo.controller.user.orderlist;
    opens com.example.demo.controller.user.orderlist to javafx.fxml;
    exports com.example.demo.controller.user.report;
    opens com.example.demo.controller.user.report to javafx.fxml;
    exports  com.example.demo.controller.user.translation;
    opens com.example.demo.controller.user.translation to javafx.fxml;
    exports com.example.demo.controller.user.report.showreport;
    opens com.example.demo.controller.user.report.showreport to javafx.fxml;
    exports com.example.demo.controller.user.checkprice;
    opens com.example.demo.controller.user.checkprice to javafx.fxml;
    exports com.example.demo.controller.user.closeshift;
    opens com.example.demo.controller.user.closeshift to javafx.fxml;
    exports com.example.demo.controller.admin.addproduct;
    opens com.example.demo.controller.admin.addproduct to javafx.fxml;
    exports com.example.demo.controller.user.starttheday;
    opens com.example.demo.controller.user.starttheday to javafx.fxml;
    exports com.example.demo.controller.user.starttheday.startthedaysuccess;
    opens com.example.demo.controller.user.starttheday.startthedaysuccess to javafx.fxml;
    exports com.example.demo.controller.admin.account;
    opens com.example.demo.controller.admin.account to javafx.fxml;
    exports com.example.demo.controller.admin.dashboard;
    opens com.example.demo.controller.admin.dashboard to javafx.fxml;
    exports com.example.demo.controller.admin.product;
    opens com.example.demo.controller.admin.product to javafx.fxml;
    exports com.example.demo.controller.admin.voucher;
    opens com.example.demo.controller.admin.voucher to javafx.fxml;
    exports com.example.demo.controller.user.endday;
    opens com.example.demo.controller.user.endday to javafx.fxml;
    exports com.example.demo.controller.user.closeshift.closeshiftsuccess;
    opens com.example.demo.controller.user.closeshift.closeshiftsuccess to javafx.fxml;
    exports com.example.demo.controller.user.cash;
    opens com.example.demo.controller.user.cash to javafx.fxml;
    exports com.example.demo.controller.user.pay;
    opens com.example.demo.controller.user.pay to javafx.fxml;
    exports com.example.demo.controller.user.QR;
    opens com.example.demo.controller.user.QR to javafx.fxml;

}
