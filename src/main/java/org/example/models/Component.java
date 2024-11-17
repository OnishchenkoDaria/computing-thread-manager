package org.example.models;

public class Component {
    //for id to be unable to modify
    private final int index;
    //unable to change component's function
    private final MathFunctions function;

    //for summary
    private String status = "created";
    private Integer result;

    public Component(int index, MathFunctions function) {
        this.index = index;
        this.function = function;
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

    public int run(int x) {
        return function.apply(x);
    }
}
