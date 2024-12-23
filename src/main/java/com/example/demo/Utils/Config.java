package com.example.demo.Utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.time.LocalDate;


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


    //Lấy ngày giờ hiện tại
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static String getday(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = currentTime.format(formatter);
        return formattedDateTime;
    }
}
