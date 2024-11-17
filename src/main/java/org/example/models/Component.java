package org.example.models;

import java.io.*;

public class Component implements Runnable {
    private final int index;
    private final MathFunctions function;
    private final Group group;
    private final PipedInputStream inputStream;
    private final PipedOutputStream outputStream;
    private String status = "created";
    private Integer result;

    public Component(int index, MathFunctions function, Group group,
                     PipedInputStream inputStream, PipedOutputStream outputStream) {
        this.index = index;
        this.function = function;
        this.group = group;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public int getIndex() {
        return index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public PipedOutputStream getOutputStream() {
        return outputStream;
    }

    //getting command of Piped and starting separate thread
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String command;
            while ((command = reader.readLine()) != null) {
                if ("run".equalsIgnoreCase(command)) {
                    setStatus("computing");
                    result = function.apply(group.getX()); // Використовуємо x із групи
                    setStatus("finished");
                    System.out.println("Component " + index + " finished with result: " + result);
                }
            }
        } catch (IOException e) {
            setStatus("failed");
            System.out.println("Error in component " + index + ": " + e.getMessage());
        }
    }
}
