package ru.rtk.core;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Utils {
    public static DateTimeFormatter dateAndTime() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    public static String inputString(String title, int minSize, boolean isEmpty) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(title);
            String input = scanner.nextLine().trim();
            if (!isEmpty && input.isEmpty()) {
                System.out.println("Значение не может быть пустым");
                continue;
            }

            if (input.length() < minSize) {
                System.out.printf("Значение не может быть короче %d символов\n", minSize);
                continue;
            }

            return input;
        }
    }

    public static int inputInt(String title, int minValue) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(title);
            String input = scanner.nextLine().trim();
            int value;
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException | NullPointerException nfe) {
                System.out.printf("Неправильно введено значение '%s'.\n", input);
                continue;
            }

            if (value < minValue) {
                System.out.printf("Значение не может быть меньше %d\n", minValue);
                continue;
            }

            return value;
        }
    }

    public static double inputDouble(String title, double minValue) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(title);
            String input = scanner.nextLine().trim();
            double value;
            try {
                value = Double.parseDouble(input);
            } catch (NumberFormatException | NullPointerException nfe) {
                System.out.printf("Неправильно введено значение '%s'.\n", input);
                continue;
            }

            if (value < minValue) {
                System.out.printf("Значение не может быть меньше %.2f\n", minValue);
                continue;
            }

            return value;
        }
    }
}
