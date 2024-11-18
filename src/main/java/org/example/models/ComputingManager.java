package org.example.models;

import java.util.Scanner;

//MAIN thread

//receives input & starts computing distribution
//uses PipedInputStream/PipedOutputStream to parse data

//model variant: single process - multiple threads

public class ComputingManager {
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
            } else if (input.equals("c")) {
                threadsManager.cancelRunning();
                System.out.println("Running computations manually interrupted.");
            } else if (input.equals("exit")) {
                threadsManager.shutdown();
                System.out.println("Program exited.");
                break;
            } else {
                System.out.println("Unknown command.");
            }
        }
    }

    //group <x> limit <time>
    private static void handleGroupCommand(String input) {
        try {
            String[] parts = input.split(" ");
            if (!"limit".equals(parts[2])) {
                throw new IllegalArgumentException("Invalid format for group command.");
            }
            int x = Integer.parseInt(parts[1]);
            int timeLimit = Integer.parseInt(parts[3]);
            String response = threadsManager.createGroup(x, timeLimit);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Invalid group command. Correct format: group <x> limit <time>");
        }
    }

    //new <function> limit <time>
    private static void handleNewComponentCommand(String input) {
        try {
            String[] parts = input.split(" ");
            if (!"limit".equals(parts[2])) {
                throw new IllegalArgumentException("Invalid format for new component command.");
            }
            String functionSymbol = parts[1];
            int timeLimit = Integer.parseInt(parts[3]);

            String response = threadsManager.createComponent(1, functionSymbol, timeLimit);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Invalid new component command. Correct format: new <function> limit <time>");
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadsManager.cancelRunning();
            threadsManager.shutdown();
            System.out.println("\nExecution interrupted by Ctrl+C. Shutting down...");
        }));

        start();
    }
}
