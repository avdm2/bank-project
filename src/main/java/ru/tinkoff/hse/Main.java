package ru.tinkoff.hse;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("err: len(args) != 2");
            return;
        }

        // comment
        try {
            int num1 = Integer.parseInt(args[0]);
            int num2 = Integer.parseInt(args[1]);
            System.out.println(num1 + "+" + num2 + "=" + (num1 + num2));
        } catch (NumberFormatException e) {
            System.out.println("err: check input");
        }
    }
}
