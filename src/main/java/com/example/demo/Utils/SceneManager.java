package com.example.demo.Utils;

import javafx.scene.Scene;
import java.util.Stack;

public class SceneManager {

    private static SceneManager instance;
    private final Stack<Scene> sceneHistory = new Stack<>();

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void addScene(Scene scene) {
        sceneHistory.push(scene);
    }

    public Scene getPreviousScene() {
        if (sceneHistory.size() > 1) {
            sceneHistory.pop();
            return sceneHistory.peek();
        }
        return null;
    }
}

