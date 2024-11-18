package org.example.models;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;

public class ThreadsManager {
    private final Map<Integer, Group> groups = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<Integer, Future<?>> runningTasks = new ConcurrentHashMap<>();
    private int groupIndex = 0;
    private int componentIndex = 0;

    public String createGroup(int x, int timeLimit) {
        if (x < 0) {
            return "Error: x must be a non-negative integer.";
        }
        groupIndex++;
        Group group = new Group(groupIndex, x, timeLimit);
        groups.put(groupIndex, group);
        return "New group " + groupIndex + " with x=" + x + " and time limit=" + timeLimit + "s.";
    }

    public String createComponent(Integer groupIndex, String functionSymbol, int timeLimit) {
        Group group = groups.get(groupIndex);
        if (group == null) {
            return "Error: Group " + groupIndex + " does not exist.";
        }

        MathFunctions function = MathFunctions.fromSymbol(functionSymbol);
        if (function == null) {
            return "Error: Function " + functionSymbol + " is not recognized.";
        }

        try {
            PipedInputStream inputStream = new PipedInputStream();
            PipedOutputStream outputStream = new PipedOutputStream(inputStream);

            Component newComponent = new Component(componentIndex++, function, group, inputStream, outputStream, timeLimit);
            group.addComponent(newComponent);

            return "Computation component " + newComponent.getIndex() +
                    " with " + functionSymbol + " added to group " + groupIndex + " with time limit=" + timeLimit + "s.";
        } catch (IOException e) {
            return "Error: Failed to create component: " + e.getMessage();
        }
    }

    public void run() {
        System.out.println("Computing...");
        groups.values().forEach(group -> group.getComponents().forEach(component -> {
            Future<?> future = executorService.submit(component);
            runningTasks.put(component.getIndex(), future);
        }));
        System.out.println("Computations started.");
    }

    public void summary() {
        System.out.println("=== Summary ===");
        groups.values().forEach(group -> group.getComponents().forEach(component -> {
            System.out.println("Component " + component.getIndex() + ": status=" + component.getStatus());
        }));
    }

    //interrupting run
    public void cancelRunning() {
        System.out.println("Cancelling running tasks...");
        runningTasks.values().forEach(task -> task.cancel(true));
        runningTasks.clear();
    }

    public void shutdown() {
        System.out.println("Shutting down...");
        cancelRunning();
        executorService.shutdownNow();
        groups.values().forEach(group -> group.getComponents().forEach(component -> {
            try {
                component.getInputStream().close();
                component.getOutputStream().close();
            } catch (IOException e) {
                System.out.println("Error closing streams for component " + component.getIndex() + ": " + e.getMessage());
            }
        }));
    }
}
