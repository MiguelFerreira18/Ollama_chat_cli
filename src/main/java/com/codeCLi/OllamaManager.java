package com.codeCLi;

import tools.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class OllamaManager {
    private final Map<Integer, Model> models = new HashMap<>();
    private final OllamaService ollamaService = new OllamaService();
    private boolean shouldPlan = false;
    private boolean shouldSaveChatHistory = false;
    private final int historyLimit = 20;
    private Deque<ChatHistory> chatHistories = new ArrayDeque<>(historyLimit);

    public OllamaManager() {
        initModels(OllamaSettings.getInstance());
    }

    private void initModels(OllamaSettings settings) {
        getModels(settings.url());
    }

    private void getModels(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "/tags")).GET().build();

        try {
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            List<Model> models = ollamaService.toModels(response.body());
            mapModels(models);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mapModels(List<Model> models) {
        for (int i = 0; i < models.size(); ++i) {
            this.models.put(i, models.get(i));
        }
    }

    public void printAvailableModels() {
        for (Map.Entry<Integer, Model> entry : models.entrySet()) {
            System.out.println("("+entry.getKey() + ") - " + entry.getValue().name());
        }
    }

    public ModelResponse prompt(int model, String question) {
        if (this.shouldSaveChatHistory) {
            question = this.getChatHistory() + question;
        }
        Prompt prompt = new Prompt(models.get(model).name(), question, false);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OllamaSettings.getInstance().url() + "/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(ollamaService.promptToString(prompt)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return ollamaService.toResponse(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void renderResponse(String response) {
        System.out.println();
        String coloredResponse = CliRender.renderResponse(response);
        System.out.println(coloredResponse);
    }

    public int getNumberOfModels() {
        return models.size();
    }

    public void addChatHistory(ChatHistory chatHistory) {
        if (this.chatHistories.size() == historyLimit) this.chatHistories.pollLast();
        this.chatHistories.addFirst(chatHistory);
    }

    public String getChatHistory() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        sb.append("=================Chat history============");
        for (ChatHistory history : this.chatHistories) {
            sb.append(++i).append(" ª chat:");
            sb.append("Question:").append("\n");
            sb.append(history.question()).append("\n");
            sb.append("Response:").append("\n");
            sb.append(history.response()).append("\n");
            sb.append("-------------------------------\n");
        }
        sb.append("=================End of chat history============");
        return sb.toString();
    }

    public void resetDefaults() {
        this.shouldSaveChatHistory = false;
        this.shouldPlan = false;
        cleanChatHistory();
    }

    private void cleanChatHistory() {
        this.chatHistories = new ArrayDeque<>();
    }

    public void switchShouldPlan() {
        this.shouldPlan = !this.shouldPlan;
    }

    public boolean shouldPlan() {
        return shouldPlan;
    }

    public void switchShouldSaveChatHistory() {
        this.shouldSaveChatHistory = !shouldSaveChatHistory;
    }

    public boolean shouldSaveChatHistory() {
        return shouldSaveChatHistory;
    }
}
