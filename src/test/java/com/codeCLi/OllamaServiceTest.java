package com.codeCLi;

import com.codeCLi.records.Model;
import com.codeCLi.records.ModelResponse;
import com.codeCLi.records.Prompt;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OllamaServiceTest {

    private final OllamaService service = new OllamaService();

    @Test
    void testToModel() {
        String json = "{\"name\":\"llama2\",\"model\":\"llama2:latest\",\"size\":3791730554,\"detail\":{\"format\":\"gguf\",\"family\":\"llama\",\"parameter_size\":\"7B\"}}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        Model model = service.toModel(is);
        assertEquals("llama2", model.name());
        assertEquals(3791730554L, model.size());
        assertEquals("gguf", model.detail().format());
    }

    @Test
    void testToModels() {
        String json = "{\"models\":[{\"name\":\"llama2\",\"model\":\"llama2:latest\",\"size\":3791730554,\"detail\":{\"format\":\"gguf\",\"family\":\"llama\",\"parameter_size\":\"7B\"}}]}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        List<Model> models = service.toModels(is);
        assertEquals(1, models.size());
        assertEquals("llama2", models.get(0).name());
    }

    @Test
    void testToResponse() {
        String json = "{\"model\":\"llama2\",\"response\":\"Hello there!\"}";
        ModelResponse response = service.toResponse(json);
        assertEquals("llama2", response.model());
        assertEquals("Hello there!", response.response());
    }

    @Test
    void testPromptToString() {
        Prompt prompt = new Prompt("llama2", "Hi", false);
        String json = service.promptToString(prompt);
        assertTrue(json.contains("\"model\":\"llama2\""));
        assertTrue(json.contains("\"prompt\":\"Hi\""));
        assertTrue(json.contains("\"stream\":false"));
    }
}
