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
class MainTest {

    @Mock
    private OllamaManager manager;

    @Mock
    private Scanner scanner;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        Main.scan = scanner;
    }

    @AfterEach
    void restore() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintMenu() {
        Main.printMenu(manager);
        verify(manager).printAvailableModels();
        assertTrue(outContent.toString().contains("(-1) - Return"));
    }

    @Test
    void testSelectModel() {
        when(scanner.nextLine()).thenReturn("0");
        when(manager.getNumberOfModels()).thenReturn(1);
        
        int selected = Main.selectModel(manager);
        assertEquals(0, selected);
    }

    @Test
    void testInputMessagePrompt() {
        when(scanner.nextLine()).thenReturn("Hello");
        when(manager.prompt(anyInt(), anyString())).thenReturn(new ModelResponse("llama2", "Hi"));

        boolean result = Main.inputMessage(manager, 0);
        assertFalse(result);
        verify(manager).prompt(eq(0), contains("Hello"));
    }

    @Test
    void testInputMessageCommandPlan() {
        when(scanner.nextLine()).thenReturn("/plan");
        when(manager.shouldPlan()).thenReturn(true);

        boolean result = Main.inputMessage(manager, 0);
        assertFalse(result);
        verify(manager).switchShouldPlan();
        assertTrue(outContent.toString().contains("planning mode"));
    }

    @Test
    void testInputMessageCommandModel() {
        when(scanner.nextLine()).thenReturn("/model");

        boolean result = Main.inputMessage(manager, 0);
        assertTrue(result);
        verify(manager).resetDefaults();
    }
}
