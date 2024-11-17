package org.example.models;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//manages parallel component work
//creates, runs & reports work of threads

public class ThreadsManager {
    private final Map<Integer, Group> groups = new ConcurrentHashMap<>();
    private int groupIndex = 0;
    private int componentIndex = 0;

    public String createGroup(int x) {
        if (x < 0) {
            return "Error: x must be a non-negative integer.";
        }
        groupIndex++;
        groups.put(groupIndex, new Group(groupIndex, x));
        return "new group " + groupIndex + " with x=" + x;
    }

    public String createComponent(Integer groupIndex, String functionSymbol) {
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

            Component newComponent = new Component(componentIndex++, function, group, inputStream, outputStream);
            group.addComponent(newComponent);

            new Thread(newComponent).start();
            return "computation component " + newComponent.getIndex() +
                    " with " + functionSymbol + " added to group " + groupIndex;
        } catch (IOException e) {
            return "Error: Failed to create component: " + e.getMessage();
        }
    }

    public void sendCommand(Integer groupIndex, String command) {
        Group group = groups.get(groupIndex);
        if (group == null) {
            System.out.println("Error: Group " + groupIndex + " does not exist.");
            return;
        }

        group.getComponents().forEach(component -> {
            try {
                component.getOutputStream().write((command + "\n").getBytes());
                component.getOutputStream().flush();
            } catch (IOException e) {
                System.out.println("Error: Failed to send command to component " + component.getIndex());
            }
        });
    }

    public void run() {
        System.out.println("computing");
        groups.values().forEach(group -> sendCommand(group.getIndex(), "run"));
    }

    public void summary() {
        System.out.println("=== Summary ===");
        groups.values().forEach(group -> group.getComponents().forEach(component -> {
            System.out.println("Component " + component.getIndex() + ": status=" + component.getStatus());
        }));
        System.out.println("computation finished");
    }
}
