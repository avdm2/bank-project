package ru.tinkoff.hse;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a, b;

        try {
            a = scanner.nextInt();
            b = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("err");
            return;
        }
        finally {
            scanner.close();
        }

        System.out.println(a + b);
    }
}
