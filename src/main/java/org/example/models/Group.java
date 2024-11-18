package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final int index;
    private final int x;
    private final int timeLimit; // Time limit for the group
    private final List<Component> components = new ArrayList<>();

    public Group(int index, int x, int timeLimit) {
        this.index = index;
        this.x = x;
        this.timeLimit = timeLimit;
    }

    public int getIndex() {
        return index;
    }

    public int getX() {
        return x;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }
}
