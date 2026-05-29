package com.codeCLi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OllamaManagerTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private OllamaService ollamaService;

    @Mock
    private HttpResponse<InputStream> inputStreamResponse;

    @Mock
    private HttpResponse<String> stringResponse;

    private OllamaManager ollamaManager;

    @BeforeEach
    void setUp() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(inputStreamResponse);
        when(ollamaService.toModels(any())).thenReturn(List.of(new Model("llama2", "llama2", 0, null)));
        
        ollamaManager = new OllamaManager(httpClient, ollamaService);
    }

    @Test
    void testInitModels() {
        assertEquals(1, ollamaManager.getNumberOfModels());
    }

    @Test
    void testPrompt() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(stringResponse);
        when(stringResponse.body()).thenReturn("{\"model\":\"llama2\",\"response\":\"Hello\"}");
        when(ollamaService.toResponse(anyString())).thenReturn(new ModelResponse("llama2", "Hello"));
        when(ollamaService.promptToString(any())).thenReturn("{}");

        ModelResponse response = ollamaManager.prompt(0, "Hi");
        assertNotNull(response);
        assertEquals("Hello", response.response());
    }

    @Test
    void testPrintAvailableModels() {
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));
        try {
            ollamaManager.printAvailableModels();
            assertTrue(outContent.toString().contains("(0) - llama2"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testRenderResponse() {
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));
        try {
            ollamaManager.renderResponse("Some response");
            assertFalse(outContent.toString().isEmpty());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testChatHistory() {
        ollamaManager.addChatHistory(new ChatHistory("Q1", "A1"));
        String history = ollamaManager.getChatHistory();
        assertTrue(history.contains("Q1"));
        assertTrue(history.contains("A1"));
    }

    @Test
    void testSwitches() {
        assertFalse(ollamaManager.shouldPlan());
        ollamaManager.switchShouldPlan();
        assertTrue(ollamaManager.shouldPlan());

        assertFalse(ollamaManager.shouldSaveChatHistory());
        ollamaManager.switchShouldSaveChatHistory();
        assertTrue(ollamaManager.shouldSaveChatHistory());

        ollamaManager.resetDefaults();
        assertFalse(ollamaManager.shouldPlan());
        assertFalse(ollamaManager.shouldSaveChatHistory());
    }
}
