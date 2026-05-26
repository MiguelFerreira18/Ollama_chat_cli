package com.codeCLi;

import java.util.Optional;

public enum Command {
    PLAN("/plan"),
    SAVE_HISTORY("/save_history"),
    EXIT("/exit"),
    MODEL("/model");

    private final String trigger;

    Command(String trigger) {
        this.trigger = trigger;
    }


    public static Optional<Command> parse(String input){
        String[] tokens = input.trim().split("\\s+");
        if (tokens.length <= 1) {
            for (String token : tokens) {
                for (Command cmd : values()) {
                    if (cmd.trigger.equalsIgnoreCase(token)) {
                        return Optional.of(cmd);
                    }
                }
            }
        }
        return Optional.empty();
    }
}
