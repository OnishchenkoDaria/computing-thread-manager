package org.example.models;

import java.util.function.Function;

//single parameter math functions

enum MathFunctions {
    SQUARE(x -> x * x),
    INCREMENT(x -> x + 1),
    SQUARE_ROOT(x -> (int) Math.sqrt(x)),
    FACTORIAL(x -> factorial(x)),

    //failed versions with intentional delays
    FAKESQUARE(x -> fakeDelay(y -> y * y, x)),
    FAKEINCREMENT(x -> fakeDelay(y -> y + 1, x)),
    FAKESQUARE_ROOT(x -> fakeDelay(y -> (int) Math.sqrt(y), x)),
    FAKEFACTORIAL(x -> fakeDelay(MathFunctions::factorial, x));

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

    private static int fakeDelay(Function<Integer, Integer> operation, int input) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during fake delay.");
        }
        //execute operation
        return operation.apply(input);
    }
}