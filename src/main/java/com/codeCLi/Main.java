package com.codeCLi;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static Scanner scan;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        int port = 11434;

        if (args.length > 0) {
            port = Integer.parseInt(args[0].trim().split("=")[1]);
        } else {
            System.out.println("Not port was provided, assuming default port: 11434\n");
        }

        OllamaSettings.getInstance(port);
        OllamaManager manager = new OllamaManager();
        int model = selectModel(manager);

        if (model == -1) {
            clear();
            System.exit(68);
        }

        do {
            if (inputMessage(manager, model)) {
                model = selectModel(manager);
                if (model == -1) {
                    clear();
                    System.exit(68);
                }
            }
        } while (true);
    }

    public static void printMenu(OllamaManager manager) {
        manager.printAvailableModels();
        System.out.println("(-1) - Return");
    }

    public static int selectModel(OllamaManager manager) {
        int input;

        do {
            printMenu(manager);
            try {
                input = Integer.parseInt(scan.nextLine().trim());
            } catch (NumberFormatException e) {
                input = 9999999;
            }
        } while (input < -1 || input > manager.getNumberOfModels() - 1);

        return input;
    }

    public static void clear() {
        System.out.print("\033[H\033[2J\033[3A");
        System.out.flush();
    }

    /**
     * Gets input from the user, this can be a prompt or a command to exit the program, or select another model
     *
     * @param manager Ollama manager
     * @param model   the user selected model
     * @return true to change the model or false to continue with the same model
     */
    public static boolean inputMessage(OllamaManager manager, int model) {
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
            renderResponse(manager, model, message);
        }

        return false;
    }

    public static void renderResponse(OllamaManager manager, int model, String message) {
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
}