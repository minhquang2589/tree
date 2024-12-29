package com.example.demo.controller.user;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.controller.ProductSearchAdapter;
import com.example.demo.model.ProductSearch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.*;
import java.util.prefs.BackingStoreException;

public class OrderController {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ProductSearch.class, new ProductSearchAdapter())
            .create();

    public static void saveOrders(Map<String, List<ProductSearch>> orders) {
        for (Map.Entry<String, List<ProductSearch>> entry : orders.entrySet()) {
            String json = gson.toJson(entry.getValue());
            PreferencesUtils.save(entry.getKey(), json);
        }
    }


    public static Map<String, List<ProductSearch>> loadOrders() throws BackingStoreException {
        Map<String, List<ProductSearch>> orders = new HashMap<>();
        String[] keys = PreferencesUtils.preferences.keys();

        for (String key : keys) {
            if (key.startsWith("Order_")) {
                String json = PreferencesUtils.preferences.get(key, null);
                if (json != null && !json.isEmpty()) {
                    try {
                        ProductSearch[] productsArray = gson.fromJson(json, ProductSearch[].class);
                        List<ProductSearch> products = Arrays.asList(productsArray);
                        orders.put(key, products);
                    } catch (JsonSyntaxException _) {

                    }
                }
            }
        }
        return orders;
    }


    public static void removeOrder(String key) throws BackingStoreException {
        if (PreferencesUtils.preferences.get(key, null) != null) {
            PreferencesUtils.preferences.remove(key);
            PreferencesUtils.preferences.flush();
        }
    }


}
