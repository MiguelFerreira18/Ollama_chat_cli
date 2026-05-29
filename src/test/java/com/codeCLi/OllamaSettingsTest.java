package com.codeCLi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OllamaSettingsTest {

    @Test
    void testSingletonAndGetters() {
        OllamaSettings settings = OllamaSettings.getInstance();
        assertNotNull(settings);
        assertEquals(11434, settings.ollamPort());
        assertEquals("http://localhost:11434/api", settings.url());

        OllamaSettings settingsWithPort = OllamaSettings.getInstance(12345);
        // Since it's a singleton and already initialized, it should still be the same instance
        assertSame(settings, settingsWithPort);
        assertEquals(11434, settingsWithPort.ollamPort());
    }
}
