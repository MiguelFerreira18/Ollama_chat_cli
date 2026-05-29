package com.codeCLi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CliRenderTest {

    @Test
    void testRenderResponse() {
        String markdown = "# Header 1\n## Header 2\n**Bold**\n*Italic*\n`code`\n```\nblock\n```\nNormal text.";
        String rendered = CliRender.renderResponse(markdown);

        assertTrue(rendered.contains(CliRender.H1));
        assertTrue(rendered.contains(CliRender.H2));
        assertTrue(rendered.contains(CliRender.BOLD));
        assertTrue(rendered.contains(CliRender.ITALIC));
        assertTrue(rendered.contains(CliRender.CODE));
        assertTrue(rendered.contains(CliRender.RESET));
        assertTrue(rendered.contains("Header 1"));
        assertTrue(rendered.contains("Bold"));
        assertTrue(rendered.contains("block"));
    }

    @Test
    void testEmptyRender() {
        String rendered = CliRender.renderResponse("");
        assertEquals("", rendered);
    }
}
