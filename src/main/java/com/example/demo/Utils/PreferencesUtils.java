package com.example.demo.Utils;

import com.example.demo.model.User;

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
        int id = (int) get("user_id", 0);
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


}
