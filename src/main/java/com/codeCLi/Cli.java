package com.codeCLi;

import com.codeCLi.records.ChatHistory;
import com.codeCLi.records.ModelResponse;

import java.util.Optional;
import java.util.Scanner;

public class Cli {
    private final Scanner scan;
    private final OllamaManager manager;

    public Cli(Scanner scan, int port) {
        this.scan = scan;
        this.manager = new OllamaManager();
    }

    Cli(Scanner scan, OllamaManager manager) {
        this.scan = scan;
        this.manager = manager;
    }

    public void startCli() {
        int model = selectModel();

        if (model == -1) {
            clear();
            System.exit(68);
        }

        do {
            if (inputMessage( model)) {
                model = selectModel();
                if (model == -1) {
                    clear();
                    System.exit(68);
                }
            }
        } while (true);
    }

    int selectModel() {
        int input;

        do {
            printMenu();
            try {
                input = Integer.parseInt(this.scan.nextLine().trim());
            } catch (NumberFormatException e) {
                input = 9999999;
            }
        } while (input < -1 || input > manager.getNumberOfModels() - 1);

        return input;
    }

    /**
     * Gets input from the user, this can be a prompt or a command to exit the program, or select another model
     *
     * @param model   the user selected model
     * @return true to change the model or false to continue with the same model
     */
    boolean inputMessage(int model) {
        String message = scan.nextLine();
        Optional<Command> cmd = Command.parse(message);

        if (cmd.isPresent()) {
            switch (cmd.get()) {
                case MODEL -> {
                    clear();
                    manager.resetDefaults();
                    return true;
                }
                case PLAN -> {
                    manager.switchShouldPlan();
                    if (manager.shouldPlan()) {
                        System.out.println("\nThe model is on planning mode");
                    } else {
                        System.out.println("\nThe model is on normal mode");
                    }
                }
                case SAVE_HISTORY -> {
                    manager.switchShouldSaveChatHistory();
                    if (manager.shouldSaveChatHistory()) {
                        System.out.println("\nThe chat history is being saved");
                    } else {
                        System.out.println("\nThe chat history stopped being saved");
                    }
                }
                case EXIT -> System.exit(68);
            }
        } else {
            renderResponse( model, message);
        }

        return false;
    }

    void renderResponse(int model, String message) {
        if (manager.shouldPlan()) {
            message = "You are in planning mode, Your task is to produce a plan only — do not execute or answer the request directly.\n" +
                    "Break down the following into clear, actionable steps a person can follow:\n\n" +
                    message;
        }

        ModelResponse modelResponse = manager.prompt(model, message);
        if (manager.shouldSaveChatHistory()) {
            manager.addChatHistory(new ChatHistory(message, modelResponse.response()));
        }
        manager.renderResponse(modelResponse.response());
    }

    private void clear() {
        System.out.print("\033[H\033[2J\033[3A");
        System.out.flush();
    }

    void printMenu() {
        manager.printAvailableModels();
        System.out.println("(-1) - Return");
    }

}
