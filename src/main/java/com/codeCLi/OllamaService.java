package com.codeCLi;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class OllamaService {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Model toModel(InputStream stream) {
        try {
            return OBJECT_MAPPER.readValue(stream, Model.class);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Model> toModels(InputStream stream) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(stream);
            return OBJECT_MAPPER.convertValue(root.get("models"), new TypeReference<List<Model>>() {
            });
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public ModelResponse toResponse(String body) {
        try {
            return OBJECT_MAPPER.readValue(body, new TypeReference<ModelResponse>() {
            });
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public String promptToString(Prompt prompt) {
        return OBJECT_MAPPER.writeValueAsString(prompt);
    }

    public OllamaService() {
    }
}
