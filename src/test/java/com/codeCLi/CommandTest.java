package com.codeCLi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void testShouldParse() {
    }

    @ParameterizedTest
    @CsvSource({
            "/plan",
            "/save_history",
            "/model",
            "/exit",
            "/ExIt",
            "/PLAN",
            "/Save_History",
            "/MoDeL",
    })
    void testShouldParse(String command) {
        Optional<Command> cmd = Command.parse(command);
        assertTrue(cmd.isPresent());
        assertEquals(cmd.get().name().toLowerCase(),command.split("/")[1].toLowerCase());
    }

    @ParameterizedTest
    @CsvSource({
            "/spyware",
            "/bloat",
            "/min_command",
            "/what_command",
    })
    void testShouldNotParse(String command){
        Optional<Command> cmd = Command.parse(command);
        assertFalse(cmd.isPresent());
    }



}