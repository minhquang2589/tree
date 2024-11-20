package com.example.demo.Utils;
import com.example.demo.model.UserModel;
import java.util.prefs.Preferences;


public class PreferencesUtils {

    private static final Preferences preferences = Preferences.userNodeForPackage(PreferencesUtils.class);

    public static void save(String key, Object value) {
        if (value instanceof String) {
            preferences.put(key, (String) value);
        } else if (value instanceof Boolean) {
            preferences.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            preferences.putInt(key, (Integer) value);
        } else if (value instanceof Double) {
            preferences.putDouble(key, (Double) value);
        } else if (value instanceof Long) {
            preferences.putLong(key, (Long) value);
        } else if (value == null) {
            preferences.remove(key);
        } else {
            throw new IllegalArgumentException(String.valueOf(value.getClass()));
        }
    }

    public static Object get(String key, Object defaultValue) {
        if (defaultValue instanceof String) {
            return preferences.get(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return preferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return preferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Double) {
            return preferences.getDouble(key, (Double) defaultValue);
        } else if (defaultValue instanceof Long) {
            return preferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue == null) {
            return null;
        } else {
            throw new IllegalArgumentException(String.valueOf(defaultValue.getClass()));
        }
    }

    public static void remove(String key) {
        preferences.remove(key);
    }

    public static void clearAll() {
        try {
            preferences.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(UserModel user) {
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

    public static UserModel getUser() {
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
            return new UserModel(id, fullName, email, phone, gender, role, birthday, image, pass, address);
        }

        return null;
    }


}
