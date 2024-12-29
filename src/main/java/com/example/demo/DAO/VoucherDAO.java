package com.example.demo.DAO;

import com.example.demo.model.Voucher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoucherDAO {

    public static boolean updateVoucher(Connection connection, Voucher voucher) {
        String updateQuery = """
                UPDATE vouchers
                SET voucher_quantity = voucher_quantity - 1,
                    status = CASE WHEN voucher_quantity - 1 = 0 THEN 'inactive' ELSE status END
                WHERE voucher_id = ? AND voucher_quantity > 0;
                """;

        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setInt(1, voucher.getVoucherId());
            int affectedRows = stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
