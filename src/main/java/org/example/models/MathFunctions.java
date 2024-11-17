package org.example.models;

import java.util.function.Function;

enum MathFunctions {
    SQUARE(x -> x * x),
    INCREMENT(x -> x + 1);

    private final Function<Integer, Integer> operation;

    MathFunctions(Function<Integer, Integer> operation) {
        this.operation = operation;
    }

    public int apply(int x) {
        return operation.apply(x);
    }

    public static MathFunctions fromSymbol(String symbol) {
        try {
            return MathFunctions.valueOf(symbol.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}