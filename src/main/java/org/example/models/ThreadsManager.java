package org.example.models;

import javax.swing.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadsManager {

    private Map<Integer, Group> groups = new ConcurrentHashMap<>();

    private int groupIndex = 0;
    private int componentIndex = 0;


    public String createGroup(int x) {
        if (x < 0) {
            groupIndex++;
        }
        groups.put(groupIndex, new Group(x));
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

        Component newComponent = new Component(componentIndex++, function);
        group.addComponent(newComponent);

        return "computation component " + newComponent.getIndex() +
                " with " + functionSymbol + " added to " + groupIndex + "group";
    }

    //new <component symbol> -- create new thread, assign function
    /*public static void addNewComponent() throws IOException {
        PipedOutputStream managerToComponent = new PipedOutputStream();
        PipedInputStream componentInput = new PipedInputStream(managerToComponent);
    }*/
}
