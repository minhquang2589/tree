package com.example.demo.Utils;

import com.example.demo.model.ProductSearch;
import com.example.demo.model.Voucher;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;


public class Config {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    public static boolean isPhoneNumberValid(String phoneNumber) {
        String phoneRegex = "^0[0-9]{9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return !matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static String saveImage(String originalFileName, java.io.File file) {
        Path targetDir = Paths.get("src/main/resources/assets/images");
        if (!Files.exists(targetDir)) {
            try {
                Files.createDirectories(targetDir);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        String uniqueFileName = generateFileNameWithTimestamp(originalFileName);
        Path targetPath = ((java.nio.file.Path) targetDir).resolve(uniqueFileName);
        try {
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return targetPath.toString();
    }


    public static String generateFileNameWithTimestamp(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + fileExtension;
    }


    public static List<File> showFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
        return fileChooser.showOpenMultipleDialog(stage);
    }


    public static String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    public static String hashCodeSHA(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            BigInteger prime = new BigInteger("100000000000");
            BigInteger uniqueNumber = number.mod(prime);
            return String.format("%012d", uniqueNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String formatCurrencyVND(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(amount) + " ₫";
    }

    public static Map<String, Double> calculateCartTotal(List<ProductSearch> productList, Voucher voucher) {
        Map<String, Double> cart = new HashMap<>();
        double totalAmount = 0;
        double totalOriginalAmount = 0;
        double totalDiscount = 0;
        int totalQuantity = 0;
        double saleDiscountAmount = 0;
        double voucherDiscountAmount = 0;

        double voucherDiscountPercentage = 0;
        if (voucher != null) {
            voucherDiscountPercentage = voucher.getVoucherPercentage();
        }

        for (ProductSearch product : productList) {
            double price = product.getGia();
            int quantity = product.getSoLuong();
            double discountPercentage = product.getChietKhau();
            double productOriginalTotal = price * quantity;
            double productTotal = productOriginalTotal * (1 - discountPercentage / 100);

            double productSaleDiscount = productOriginalTotal - productTotal;
            saleDiscountAmount += productSaleDiscount;

            totalOriginalAmount += productOriginalTotal;
            totalAmount += productTotal;
            totalQuantity += quantity;
        }

        if (voucher != null) {
            voucherDiscountAmount = totalAmount * (voucherDiscountPercentage / 100);
            totalAmount -= voucherDiscountAmount;
        }

        totalDiscount = saleDiscountAmount + voucherDiscountAmount;

        cart.put("totalAmount", totalAmount);
        cart.put("totalQuantity", (double) totalQuantity);
        cart.put("saleDiscountAmount", saleDiscountAmount);
        cart.put("voucherDiscountAmount", voucherDiscountAmount);
        cart.put("totalDiscount", totalDiscount);

        return cart;
    }




}
