package com.codeCLi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OllamaManager {
    Map<Integer, Model> models = new HashMap<>();
    OllamaService ollamaService = new OllamaService();
    CliRender render = new CliRender();

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
            System.out.println(entry.getKey() + " - " + entry.getValue().name());
        }
    }

    public ModelResponse prompt(int model, String question) {
        Prompt prompt = new Prompt(models.get(model).name(),question,false);
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

    public void renderResponse(String response){
        String coloredResponse = CliRender.renderResponse(response);
        System.out.println(coloredResponse);
    }
    public int getNumberOfModels(){
        return models.size();
    }
}
