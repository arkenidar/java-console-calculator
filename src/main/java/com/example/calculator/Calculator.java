package com.example.calculator;

// JS <-> Java sync:
// - Calculator.calculate matches calc-formula.js calculate()
// - main tests mirror calc-formula.js tests

public class Calculator {

    public static void main(String[] args) {
        System.out.println("TEST #0 " + (calculate("123") == 123.0));
        System.out.println("TEST #1 " + (calculate("-31*2") == -62.0));
        System.out.println("TEST #2 " + (calculate("-31*(-2)") == 62.0));
        System.out.println("TEST #3 " + (calculate("1-(2+3+6)") == -10.0));
        System.out.println("TEST #4 " + (calculate("-(2+3+6)") == -11.0));
        System.out.println("TEST #5 " + (calculate("10+(2*(1+2)+3)") == 19.0));
        System.out.println("TEST #6 " + (calculate("5/2") == 2.5));
        System.out.println("TEST #7 " + (calculate("2^3") == 8.0));
    }

    public static double calculate(String formula) {
        System.out.println("!formula: " + formula);

        StringBuilder digits = "".equals("") ? new StringBuilder() : new StringBuilder();
        Double value = null;

        String operator = "";
        Double operand = null;

        OperFrame[] operStack = new OperFrame[formula.length() + 10];
        int operTop = -1;

        operTop = operPush(operStack, operTop);

        for (int i = 0; i < formula.length(); i++) {

            char ch = formula.charAt(i);
            System.out.println(ch + " " + (numeric(ch) || ch == '.') + " char debug");

            if (numeric(ch) || ch == '.') {
                digits.append(ch);
            }

            if (ch == '(') {
                operTop = operPush(operStack, operTop);
                value = null;
            }

            if (isOperator(ch) || i == last(formula) || ch == ')') {

                value = parse(formula, i, ch, value, digits, operStack, operTop);
                if (ch == ')') {
                    operTop--;
                    value = parse(formula, i, ch, value, digits, operStack, operTop);
                }
            }
        }

        return value != null ? value : 0.0;
    }

    private static Double parse(String formula, int i, char ch, Double value, StringBuilder digits,
                                OperFrame[] operStack, int operTop) {

        String operator = operStack[operTop].operator;
        Double operand = operStack[operTop].operand;

        if (digits.length() != 0) {
            value = Double.parseDouble(digits.toString());
            digits.setLength(0);
        }

        if (ch == '-' && value == null) value = 0.0;

        System.out.println(value + " value!");

        if (operand != null) {
            if ("+".equals(operator)) {
                value = operand + value;
            } else if ("*".equals(operator)) {
                value = operand * value;
            } else if ("-".equals(operator)) {
                value = operand - value;
            } else if ("/".equals(operator)) {
                value = operand / value;
            } else if ("^".equals(operator)) {
                value = Math.pow(operand, value);
            }
            operand = null;
        }
        System.out.println("calc! " + value);

        if (isOperator(ch)) {
            operator = String.valueOf(ch);
            operand = value;
        }

        operStack[operTop].operator = operator;
        operStack[operTop].operand = operand;

        return value;
    }

    private static int operPush(OperFrame[] stack, int top) {
        top++;
        stack[top] = new OperFrame();
        stack[top].operator = "";
        stack[top].operand = null;
        return top;
    }

    private static boolean numeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static int last(String sequence) {
        return sequence.length() - 1;
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '*' || ch == '-' || ch == '/' || ch == '^';
    }

    private static class OperFrame {
        String operator;
        Double operand;
    }

}
