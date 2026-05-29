package com.codeCLi;

import com.codeCLi.records.ChatHistory;
import com.codeCLi.records.ModelResponse;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int port = 11434;

        if (args.length > 0) {
            port = Integer.parseInt(args[0].trim().split("=")[1]);
        } else {
            System.out.println("Not port was provided, assuming default port: 11434\n");
        }

        Cli cli = new Cli(new Scanner(System.in),port);
        cli.startCli();
    }
}