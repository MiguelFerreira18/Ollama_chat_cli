package com.codeCLi;

public class OllamaSettings {
    private static OllamaSettings ollamaSettings;
    private int ollamPort = 11434;
    private final String url;

    private OllamaSettings(int ollamPort) {
        this.ollamPort = ollamPort;
        this.url = "http://localhost:" + ollamPort + "/api";
    }

    private OllamaSettings() {
        this.url = "http://localhost:" + ollamPort + "/api";
    }

    public static OllamaSettings getInstance(int port) {
        if (ollamaSettings == null) {
            ollamaSettings = new OllamaSettings(port);
        }
        return ollamaSettings;
    }

    public static OllamaSettings getInstance() {
        if (ollamaSettings == null) {
            ollamaSettings = new OllamaSettings();
        }
        return ollamaSettings;
    }

    public int ollamPort() {
        return ollamPort;
    }

    public String url() {
        return url;
    }
}
