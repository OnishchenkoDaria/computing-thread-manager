package org.example.models;

import java.util.Scanner;

//MAIN thread

//receives input & starts computing distribution
//uses PipedInputStream/PipedOutputStream to parse data

//model variant: single process - multiple threads

public class ComputingManager   {
    private static final ThreadsManager threadsManager = new ThreadsManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void start() {
        System.out.println("Enter commands:");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.startsWith("group")) {
                handleGroupCommand(input);
            } else if (input.startsWith("new")) {
                handleNewComponentCommand(input);
            } else if (input.equals("run")) {
                threadsManager.run();
            } else if (input.equals("summary")) {
                threadsManager.summary();
            } else if (input.equals("exit")) {
                break;
            } else {
                System.out.println("Unknown command.");
            }
        }
    }

    private static void handleGroupCommand(String input) {
        try {
            int x = Integer.parseInt(input.split(" ")[1]);
            String response = threadsManager.createGroup(x);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Invalid group command.");
        }
    }

    private static void handleNewComponentCommand(String input) {
        try {
            String[] parts = input.split(" ");
            int groupIndex = Integer.parseInt(parts[1]);
            String functionSymbol = parts[2];
            String response = threadsManager.createComponent(groupIndex, functionSymbol);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Invalid new component command.");
        }
    }

    public static void main(String[] args) {
        start();
    }
}
