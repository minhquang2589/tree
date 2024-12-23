package com.example.demo.Utils;

import com.example.demo.model.Shift;
import com.example.demo.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.bytebuddy.description.method.MethodDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;


public class PreferencesUtils {

    private static final Preferences preferences = Preferences.userNodeForPackage(PreferencesUtils.class);

    public static void save(String key, Object value) {
        switch (value) {
            case String s -> preferences.put(key, s);
            case Boolean b -> preferences.putBoolean(key, b);
            case Integer i -> preferences.putInt(key, i);
            case Double v -> preferences.putDouble(key, v);
            case Long l -> preferences.putLong(key, l);
            case null -> preferences.remove(key);
            default -> throw new IllegalArgumentException(String.valueOf(value.getClass()));
        }
    }

    public static Object get(String key, Object defaultValue) {
        return switch (defaultValue) {
            case String s -> preferences.get(key, s);
            case Boolean b -> preferences.getBoolean(key, b);
            case Integer i -> preferences.getInt(key, i);
            case Double v -> preferences.getDouble(key, v);
            case Long l -> preferences.getLong(key, l);
            case null -> null;
            default -> throw new IllegalArgumentException(String.valueOf(defaultValue.getClass()));
        };
    }

    public static void remove(String key) {
        preferences.remove(key);
    }

    // không dùng
    public static void clearAll() {
        try {
            preferences.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(User user) {
        save("user_id", user.getId());
        save("user_fullName", user.getName());
        save("user_email", user.getEmail());
        save("user_phone", user.getPhone());
        save("user_gender", user.getGender());
        save("user_role", user.getRole());
        save("user_birthday", user.getBirthday());
        save("user_image", user.getImage());
        save("user_password", user.getPassword());
        save("user_address", user.getAddress());
    }

    public static void clearUser() {
        remove("user_id");
        remove("user_fullName");
        remove("user_email");
        remove("user_phone");
        remove("user_gender");
        remove("user_role");
        remove("user_birthday");
        remove("user_image");
        remove("user_password");
        remove("user_address");
        remove("isLoggedIn");
    }

    public static User getUser() {
        int id = (Integer) get("user_id", 0);
        String fullName = (String) get("user_fullName", "");
        String email = (String) get("user_email", "");
        String phone = (String) get("user_phone", "");
        String gender = (String) get("user_gender", "");
        String role = (String) get("user_role", "");
        String birthday = (String) get("user_birthday", "");
        String image = (String) get("user_image", "");
        String pass = (String) get("user_password", "");
        String address = (String) get("user_address", "");

        if (id != 0 && !fullName.isEmpty()) {
            return new User(id, fullName, email, phone, gender, role, birthday, image, pass, address);
        }

        return null;
    }

    public static void saveshift(Shift shift) {
        save("500k", shift.getT500k());
        save("200k",shift.getT200k());
        save("100k",shift.getT100k());
        save("50k",shift.getT50k());
        save("20k",shift.getT20k());
        save("10k",shift.getT10k());
        save("5k",shift.getT5k());
        save("2k",shift.getT2k());
        save("1k",shift.getT1k());
        save("500",shift.getT500());
        save("200",shift.getT200());
        save("Tong",shift.getT500k());
    }

    public static void clearshift() {
        remove("500k");
        remove("200k");
        remove("100k");
        remove("50k");
        remove("20k");
        remove("10k");
        remove("5k");
        remove("2k");
        remove("1k");
        remove("500");
        remove("200");
        remove("Tong");
    }

    public static Shift getShift() {
        int t500k = (Integer) get("500k", 0);
        int t200k = (Integer) get("200k", 0);
        int t100k = (Integer) get("100k", 0);
        int t50k = (Integer) get("50k", 0);
        int t20k = (Integer) get("20k", 0);
        int t10k = (Integer) get("10k", 0);
        int t5k = (Integer) get("5k", 0);
        int t2k = (Integer) get("2k", 0);
        int t1k = (Integer) get("1k", 0);
        int t500 = (Integer) get("500", 0);
        int t200 = (Integer) get("200", 0);
        int Tong = (Integer) get("Tong", 0);
        return new Shift(t500k, t200k, t100k, t50k, t20k, t10k, t5k, t2k, t1k, t500,t200,Tong);
    }

    private static final ObservableList<Shift> shiftList = FXCollections.observableArrayList();


    public static void saveShiftList(List<Shift> shiftList) {
        int index = 0; // Index to uniquely identify each shift
        for (Shift shift : shiftList) {
            // Saving each shift with a unique key
            save("shift_" + index + "_500k", shift.getT500k());
            save("shift_" + index + "_200k", shift.getT200k());
            save("shift_" + index + "_100k", shift.getT100k());
            save("shift_" + index + "_50k", shift.getT50k());
            save("shift_" + index + "_20k", shift.getT20k());
            save("shift_" + index + "_10k", shift.getT10k());
            save("shift_" + index + "_5k", shift.getT5k());
            save("shift_" + index + "_2k", shift.getT2k());
            save("shift_" + index + "_1k", shift.getT1k());
            save("shift_" + index + "_500", shift.getT500());
            save("shift_" + index + "_200", shift.getT200());
            save("shift_" + index + "_Tong", shift.getTong());
            index++;
        }
        // Save the count of shifts for retrieval
        save("shift_count", index);  // Store the number of shifts
    }

    public static List<Shift> getShiftList() {
        List<Shift> shiftList = new ArrayList<>();
        int shiftCount = (Integer) get("shift_count", 0); // Retrieve the number of saved shifts

        // Iterate through the stored shifts
        for (int index = 0; index < shiftCount; index++) {
            int t500k = (Integer) get("shift_" + index + "_500k", 0);
            int t200k = (Integer) get("shift_" + index + "_200k", 0);
            int t100k = (Integer) get("shift_" + index + "_100k", 0);
            int t50k = (Integer) get("shift_" + index + "_50k", 0);
            int t20k = (Integer) get("shift_" + index + "_20k", 0);
            int t10k = (Integer) get("shift_" + index + "_10k", 0);
            int t5k = (Integer) get("shift_" + index + "_5k", 0);
            int t2k = (Integer) get("shift_" + index + "_2k", 0);
            int t1k = (Integer) get("shift_" + index + "_1k", 0);
            int t500 = (Integer) get("shift_" + index + "_500", 0);
            int t200 = (Integer) get("shift_" + index + "_200", 0);
            int tong = (Integer) get("shift_" + index + "_Tong", 0);

            // Create a Shift object and add it to the list
            Shift shift = new Shift(t500k, t200k, t100k, t50k, t20k, t10k, t5k, t2k, t1k, t500, t200, tong);
            shiftList.add(shift);
        }

        return shiftList;
    }


    public static void clearShiftList() {
        int shiftCount = (Integer) get("shift_count", 0); // Retrieve the number of saved shifts

        for (int index = 0; index < shiftCount; index++) {
            remove("shift_" + index + "_500k");
            remove("shift_" + index + "_200k");
            remove("shift_" + index + "_100k");
            remove("shift_" + index + "_50k");
            remove("shift_" + index + "_20k");
            remove("shift_" + index + "_10k");
            remove("shift_" + index + "_5k");
            remove("shift_" + index + "_2k");
            remove("shift_" + index + "_1k");
            remove("shift_" + index + "_500");
            remove("shift_" + index + "_200");
            remove("shift_" + index + "_Tong");
        }

        // Remove the shift count
        remove("shift_count");
    }

}
