package org.example.models;

import java.util.function.Function;

//single parameter math functions

enum MathFunctions {
    SQUARE(x -> x * x),
    INCREMENT(x -> x + 1),
    SQUARE_ROOT(x -> (int) Math.sqrt(x)),
    FACTORIAL(x -> factorial(x));

    private final Function<Integer, Integer> operation;

    //define operation attribute
    MathFunctions(Function<Integer, Integer> operation) {
        this.operation = operation;
    }

    public int apply(int x) {
        return operation.apply(x);
    }

    //searches constant definition
    public static MathFunctions fromSymbol(String symbol) {
        try {
            return MathFunctions.valueOf(symbol.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static int factorial(int x) {
        if (x == 0 || x == 1) return 1;
        int result = 1;
        for (int i = 2; i <= x; i++) {
            result *= i;
        }
        return result;
    }
}