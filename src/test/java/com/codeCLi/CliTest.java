package com.codeCLi;

import com.codeCLi.records.ModelResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CliTest {
    @Mock
    private OllamaManager manager;

    @Mock
    private Scanner scanner;

    private Cli cli;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp(){
        System.setOut(new PrintStream(outContent));
        cli = new Cli(scanner, manager);
    }

    @AfterEach
    void restore() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintMenu() {
        cli.printMenu();
        verify(manager).printAvailableModels();
        assertTrue(outContent.toString().contains("(-1) - Return"));
    }

    @Test
    void testSelectModel() {
        when(scanner.nextLine()).thenReturn("0");
        when(manager.getNumberOfModels()).thenReturn(1);

        int selected = cli.selectModel();
        assertEquals(0, selected);
    }

    @Test
    void testInputMessagePrompt() {
        when(scanner.nextLine()).thenReturn("Hello");
        when(manager.prompt(anyInt(), anyString())).thenReturn(new ModelResponse("llama2", "Hi"));

        boolean result = cli.inputMessage( 0);
        assertFalse(result);
        verify(manager).prompt(eq(0), contains("Hello"));
    }

    @Test
    void testInputMessageCommandPlan() {
        when(scanner.nextLine()).thenReturn("/plan");
        when(manager.shouldPlan()).thenReturn(true);

        boolean result = cli.inputMessage( 0);
        assertFalse(result);
        verify(manager).switchShouldPlan();
        assertTrue(outContent.toString().contains("planning mode"));
    }

    @Test
    void testInputMessageCommandModel() {
        when(scanner.nextLine()).thenReturn("/model");

        boolean result = cli.inputMessage( 0);
        assertTrue(result);
        verify(manager).resetDefaults();
    }

    @Test
    void testRenderResponse() {
        when(manager.shouldPlan()).thenReturn(false);
        when(manager.shouldSaveChatHistory()).thenReturn(false);
        when(manager.prompt(anyInt(), anyString())).thenReturn(new ModelResponse("llama2", "Response"));

        cli.renderResponse( 0, "Test Message");

        verify(manager).prompt(0, "Test Message");
        verify(manager).renderResponse("Response");
    }

    @Test
    void testRenderResponseWithPlanning() {
        when(manager.shouldPlan()).thenReturn(true);
        when(manager.shouldSaveChatHistory()).thenReturn(false);
        when(manager.prompt(anyInt(), anyString())).thenReturn(new ModelResponse("llama2", "Response"));

        cli.renderResponse( 0, "Test Message");

        verify(manager).prompt(eq(0), contains("planning mode"));
        verify(manager).renderResponse("Response");
    }
}
