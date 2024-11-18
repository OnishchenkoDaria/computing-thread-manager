package org.example.models;

import java.io.*;
import java.util.concurrent.*;

public class Component implements Runnable {
    private final int index;
    private final MathFunctions function;
    private final Group group;
    private final PipedInputStream inputStream;
    private final PipedOutputStream outputStream;
    private final int timeLimit;
    private String status = "created";
    private Integer result;

    public Component(int index, MathFunctions function, Group group,
                     PipedInputStream inputStream, PipedOutputStream outputStream, int timeLimit) {
        this.index = index;
        this.function = function;
        this.group = group;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.timeLimit = timeLimit;
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

    public PipedInputStream getInputStream() {
        return inputStream;
    }

    public PipedOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> task = executor.submit(() -> {
            try {
                setStatus("computing");
                result = function.apply(group.getX());
                setStatus("finished");
                System.out.println("Component " + index + " finished with result: " + result);
            } catch (RuntimeException e) {
                setStatus("failed");
                System.out.println("Error in component " + index + ": " + e.getMessage());
            }
        });

        try {
            if (timeLimit > 0) {
                task.get(timeLimit, TimeUnit.SECONDS);
            } else {
                task.get();
            }
        } catch (TimeoutException e) {
            setStatus("failed");
            System.out.println("Component " + index + " timed out.");
            task.cancel(true);
        } catch (Exception e) {
            setStatus("failed");
            System.out.println("Error in component " + index + ": " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }
}
