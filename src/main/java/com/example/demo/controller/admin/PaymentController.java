package com.example.demo.controller.admin;

import com.example.demo.model.ProductSearch;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.example.demo.DAO.DiscountDAO.updateDiscountRemaining;
import static com.example.demo.DAO.OrderDAO.insertOrder;
import static com.example.demo.DAO.OrderItemDAO.insertOrderItem;
import static com.example.demo.DAO.PaymentMethodDAO.findOrInsertPaymentMethod;
import static com.example.demo.DAO.VariantDAO.updateProductVariant;
import static com.example.demo.Utils.Config.calculateCartTotal;
import static com.example.demo.Utils.Config.hashCodeSHA;

public class PaymentController {

    public static String handlePayment(Connection connection, String paymentMethod, List<ProductSearch> cartItem) throws SQLException {
        if (connection != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
            String orderCode = hashCodeSHA(timestamp);
            Map<String, Double> cart = calculateCartTotal(cartItem, null);
            int paymentId = findOrInsertPaymentMethod(connection, paymentMethod, "Payment via" + " " + paymentMethod);
            int orderId = insertOrder(connection, null, paymentId, cart.get("totalAmount"), "No specific note", cart.get("totalDiscount"), orderCode);
            for (ProductSearch item : cartItem) {
                int purchasedQuantity = item.getSoLuong();
                insertOrderItem(connection, orderId, item.variantIdProperty(), purchasedQuantity);
                updateProductVariant(connection, item.variantIdProperty().get(), -purchasedQuantity);
                if (item.getChietKhau() > 0 && item.discountIdProperty() != null) {
                    updateDiscountRemaining(connection, item.discountIdProperty(), item.getSoLuong());
                }
            }
            return orderCode;
        } else {
            return null;
        }
    }

}
