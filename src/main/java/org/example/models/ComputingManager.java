package org.example.models;

import java.util.Scanner;

//MAIN thread

//receives input & starts computing distribution
//uses PipedInputStream/PipedOutputStream to parse data

//model variant: single process - multiple threads

public class ComputingManager   {
    private final ThreadsManager threadsManager = new ThreadsManager();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("Enter commands:");
    }
}
