package com.example.demo.controller.admin;

import com.example.demo.model.ProductSearch;
import com.example.demo.model.Voucher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.example.demo.DAO.DiscountDAO.updateDiscountRemaining;
import static com.example.demo.DAO.OrderDAO.insertOrder;
import static com.example.demo.DAO.OrderItemDAO.insertOrderItem;
import static com.example.demo.DAO.PaymentMethodDAO.findOrInsertPaymentMethod;
import static com.example.demo.DAO.VariantDAO.updateProductVariant;
import static com.example.demo.DAO.VoucherDAO.updateVoucher;
import static com.example.demo.Utils.Config.*;

public class PaymentController {

    public static String handlePayment(Connection connection, String paymentMethod, List<ProductSearch> cartItem, Voucher voucher) throws SQLException {
        if (connection != null) {
            String orderCode = hashCodeSHA(getCurrentDate());
            Map<String, Double> cart = calculateCartTotal(cartItem, voucher);
            System.out.println();
            if (voucher != null) {
                updateVoucher(connection, voucher);
            }
            int paymentId = findOrInsertPaymentMethod(connection, paymentMethod, "Payment via" + " " + paymentMethod);
            int orderId = insertOrder(connection, null, paymentId, cart.get("totalAmount"), "No specific note", cart.get("totalDiscount"), orderCode, voucher);
            for (ProductSearch item : cartItem) {
                int purchasedQuantity = item.getSoLuong();
                insertOrderItem(connection, orderId, item.variantIdProperty(), purchasedQuantity);
                double price = item.getGia();
                String discountId = item.discountIdProperty();
                if (discountId == null || discountId.trim().isEmpty()) {
                    discountId = null;
                }
                updateProductVariant(connection, item.getProductId(), item.getSizeId(), String.valueOf(price), purchasedQuantity, discountId, true);
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
