package com.example.demo.payment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class VNPayPayment {
    private static final String vnp_TmnCode = "JH35Y0P2";
    private static final String vnp_HashSecret = "0XTLMKBT126P90UD9Z6KZFDZORQG57D3";
    private static final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public String createPaymentUrl(String amount, String description) {
        int amountFinal = Integer.parseInt(amount) * 100;

        try {
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnp_TmnCode);
            params.put("vnp_Amount", String.valueOf(amountFinal));
            params.put("vnp_CurrCode", "VND");
            String currentTxnRef = String.valueOf(System.currentTimeMillis());
            params.put("vnp_TxnRef", currentTxnRef);

            params.put("vnp_OrderInfo", URLEncoder.encode(description, StandardCharsets.UTF_8));
            params.put("vnp_IpAddr", "127.0.0.1");
            params.put("vnp_Locale", "vn");
            params.put("vnp_OrderType", "other");
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = sdf.format(new Date(timestamp));
            params.put("vnp_CreateDate", vnpCreateDate);

            List<String> fieldKeys = new ArrayList<>(params.keySet());
            Collections.sort(fieldKeys);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String key : fieldKeys) {
                String value = URLEncoder.encode(params.get(key), StandardCharsets.UTF_8);
                if (!hashData.isEmpty()) {
                    hashData.append("&");
                }
                hashData.append(key).append("=").append(value);

                if (!query.isEmpty()) {
                    query.append("&");
                }
                query.append(key).append("=").append(value);
            }

            String secureHash = hmacSHA512(hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            return vnp_Url + "?" + query;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hmacSHA512(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(vnp_HashSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while hashing data: " + e.getMessage(), e);
        }
    }

    public BufferedImage generateQRCode(String url, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

//    public String checkTransactionStatus(String txnRef) {
//        try {
//            String url = "https://sandbox.vnpayment.vn/api/transaction-status";
//            String query = "vnp_TmnCode=" + vnp_TmnCode + "&vnp_TxnRef=" + txnRef;
//            String secureHash = hmacSHA512(query, vnp_HashSecret);
//            query += "&vnp_SecureHash=" + secureHash;
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .header("Content-Type", "application/x-www-form-urlencoded")
//                    .POST(HttpRequest.BodyPublishers.ofString(query))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            return response.body();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
