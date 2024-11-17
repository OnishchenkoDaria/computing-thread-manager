package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final int x;
    private final List<Component> components = new ArrayList<>();

    public Group(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }


}
